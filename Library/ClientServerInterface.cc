#include <iostream>
#include <memory>
#include <string>
#include <ctime>
#include <chrono>
#include <fstream>

#include <grpcpp/grpcpp.h>
#ifdef BAZEL_BUILD
#include "protos/keyvaluestore.grpc.pb.h"
#else
#include "keyvaluestore.grpc.pb.h"
#endif

#define TIMEOUT_RPC_OPERATIONS_IN_MSECS 20000
#define MAX_LENGTH_OF_KEY 129
#define MAX_LENGTH_OF_VALUE 2049

using grpc::Channel;
using grpc::ClientContext;
using grpc::Status;

std::ofstream log_file;
std::string log_file_name = "./client_lib_logs/log_";

class ClientServerInterface {
    private:
        std::unique_ptr<keyvaluestore::Stub> stub_;
        std::shared_ptr<Channel> channel;
        std::string server_endpoint;

    public:
        ClientServerInterface(std::shared_ptr<Channel> channel, std::string);

        bool checkConnectionState();
        bool check_deadline_exceeded(Status s);
        bool check_unavailable(Status s);
        std::string get_endpoint_name();
        int get_request(const std::string&, std::string&);
        int put_request(const std::string&, const std::string&, std::string&);
};

ClientServerInterface::ClientServerInterface (
        std::shared_ptr<Channel> channel,
        std::string server_endpoint
        )
    : stub_(keyvaluestore::NewStub(channel)) {
    this->channel = channel;
    this->server_endpoint = server_endpoint;
}

std::string ClientServerInterface::get_endpoint_name() {
    return this->server_endpoint;
}

bool ClientServerInterface::checkConnectionState() {
    grpc_connectivity_state state = this->channel->GetState(true);
    if(state == 1)
        return true;
    else
        return false;
}

int ClientServerInterface::get_request(const std::string& key, std::string &value) {

    /* Create Get Request */
    GetRequest request;
    request.set_key(key);
    /* Create Response */
    GetResponse response;

    // Context for the client. It could be used to convey extra information to
    // the server and/or tweak certain RPC behaviors.
    ClientContext context;

    std::chrono::system_clock::time_point deadline = std::chrono::system_clock::now() +
                          std::chrono::milliseconds(TIMEOUT_RPC_OPERATIONS_IN_MSECS);
    context.set_deadline(deadline);

    Status status = stub_->get(&context, request, &response);
    if (status.ok()) {
        if(response.responsecode() == 0)
            value = response.value();
        return response.responsecode();
    } else if(check_unavailable(status)) {
        log_file << " L: Server Unavailable!" << std::endl;
        return -2;
    } else if (check_deadline_exceeded(status)) {
        log_file << " L: Deadline exceeded!" << std::endl;
        return -1;
    } else {
        log_file << " L: " << status.error_code() << " : " << status.error_message()
                  << std::endl;
        return -1;
    }
}

bool ClientServerInterface::check_deadline_exceeded(Status s) {
    if(s.error_code() == grpc::StatusCode::DEADLINE_EXCEEDED)
        return true;
    return false;
}

bool ClientServerInterface::check_unavailable(Status s) {
    if(s.error_code() == grpc::StatusCode::UNAVAILABLE)
        return true;
    return false;
}

int ClientServerInterface::put_request(const std::string& key,
        const std::string& value,
        std::string& old_value) {

    PutRequest request;
    request.set_key(key);
    request.set_value(value);
    request.set_frommaster(0);

    PutResponse response;
    ClientContext context;
    std::chrono::system_clock::time_point deadline = std::chrono::system_clock::now() +
                                                     std::chrono::milliseconds(TIMEOUT_RPC_OPERATIONS_IN_MSECS);
    context.set_deadline(deadline);

    Status status = stub_->put(&context, request, &response);
    if(status.ok()) {
        old_value = response.oldvalue();
        return response.responsecode();
    } else if(check_unavailable(status)) {
        log_file << " L: Server Unavailable!" << std::endl;
        return -2;
    } else if (check_deadline_exceeded(status)) {
        log_file << " L: Deadline exceeded!" << std::endl;
        return -1;
    } else {
        log_file << " L: " << status.error_code() << " : " << status.error_message()
                  << std::endl;
        return -1;
    }
}

/* Constants */
const int MAX_NUMBER_SERVERS = 100;
int number_of_server_connections = 0;
ClientServerInterface *connections[MAX_NUMBER_SERVERS];
int connect_index = 0;
int log_file_created = 0;

/* Util Function prototypes */
bool is_connections_setup();
void load_balancer();
void list_connections();
void create_log_file();
bool verify_restrictions(char *value, int index);
bool check_if_string_contains_non_printable_ascii(char *s);
bool check_if_string_contains_special_character(std::string s);

/* Implementations of functions exposed to the client */
/* Init Connection request from client */
int kv739_init(char ** server_list) {
     create_log_file();

    log_file.open(log_file_name, std::ofstream::app);
    if(is_connections_setup()) {
        log_file << "L: Connection already setup!" << std::endl;
        log_file.close();
        return -1;
    }

    int index = 0;

    while(true) {

        /* Checking for null termination */
        std::string server_end_point(server_list[index]);
        if(server_end_point.compare("\0") == 0)
            break;

        /* Init Server Connections */
        log_file << "L: Initializing connection for " << server_end_point << std::endl;
        connections[index] = new ClientServerInterface(
                grpc::CreateChannel(
                        server_end_point,
                        grpc::InsecureChannelCredentials()),
                server_end_point
                        );
        index++;
    }
    number_of_server_connections = index;

    /* Check for any failure in connections */
    for(int i = 0; i < number_of_server_connections; i++) {
        if(connections[i]->checkConnectionState()) {
            log_file.close();
            return -1;
        }
    }
    connect_index = 0;
    log_file.close();
    /* On Success */
    return 0;
}

/* Get the value */
int kv739_get(char * key, char * value) {
    create_log_file();
    log_file.open(log_file_name, std::ofstream::app);

    /* Base Conditions */
    if(!is_connections_setup()) {
        log_file << "L: No connections available!" << std::endl;
        log_file.close();
        return -1;
    }
    if(key == NULL || value == NULL) {
        log_file << "L: Either key or value is NOT initialized!" << std::endl;
        log_file.close();
        return -1;
    }
    if(!verify_restrictions(key, 0)) {
        log_file << "L: Verification failed!" << std::endl;
        log_file.close();
        return -1;
    }

    /* Create Strings Key/Values */
    std::string key_request(key);
    std::string value_reply;

    int number_tries = 0;
    int response_code = 0;
    do {
        log_file << "L: Sending GET(" << key << ") request to endpoint"
                 << connections[connect_index]->get_endpoint_name() << std::endl;
        /* Send request to server */
        response_code = connections[connect_index]->get_request(key_request, value_reply);

        /* Balance load */
        load_balancer();
        //std::cout << "RC :" << response_code << std::endl;
        /* If server is unavailable, retry on different server */
        number_tries++;
    } while(number_tries < number_of_server_connections && response_code == -2);

    /* If all servers down */
    if(number_tries == number_of_server_connections && (response_code != 0 && response_code != 1)) {
        log_file << "L: All servers down!" << std::endl;
        log_file.close();
        return -1;
    }

    /* On Success */
    if(response_code == 0)
        std::copy(value_reply.begin(), value_reply.end(), value);

    log_file.close();
    return response_code;
}

/* Put function which puts the given value for the key and return (if any) old value */
int kv739_put(char * key, char * value, char * old_value) {
    create_log_file();
    log_file.open(log_file_name, std::ofstream::app);

    /* Base Conditions */
    if(!is_connections_setup()) {
        log_file << "L: No connections available!" << std::endl;
        log_file.close();
        return -1;
    }
    if(key == NULL || value == NULL) {
        log_file << "L: Either key or value is NOT initialized!" << std::endl;
        log_file.close();
        return -1;
    }
    if(!verify_restrictions(key, 0) || !verify_restrictions(value, 1)) {
        log_file << "L: Verification failed!" << std::endl;
        log_file.close();
        return -1;
    }

    /* Create Strings Key/Value/OldValue */
    std::string key_req(key);
    std::string value_req(value);
    std::string old_value_req;

    int number_tries = 0;
    int response_code = 0;
    do {
        log_file << "L: Sending PUT(" << key << "," << value << ") request to endpoint "
                 << connections[connect_index]->get_endpoint_name() << std::endl;
        /* Send Request to server */
        response_code = connections[connect_index]->put_request(key_req, value_req, old_value_req);

        /* Balance load */
        load_balancer();

        /* If server is unavailable, retry on different server */
        number_tries++;
    } while(number_tries < number_of_server_connections && response_code == -2);
    /* If all servers down */
    if(number_tries == number_of_server_connections && (response_code != 0 && response_code != 1)) {
        log_file << "L: All servers down!" << std::endl;
        log_file.close();
        return -1;
    }

    /* On Success */
    if(response_code == 0)
        std::copy(old_value_req.begin(), old_value_req.end(), old_value);

    log_file.close();
    return response_code;
}

/* Shutdown any open connection */
int kv739_shutdown() {
    create_log_file();
    log_file.open(log_file_name, std::ofstream::app);
    /* No Server to shutdown */
    if(!is_connections_setup()) {
        log_file << "L: No connections available!" << std::endl;
        log_file.close();
        return -1;
    }

    /* Closing all currently open connections */
    for(int i = 0; i < number_of_server_connections; i++) {
        log_file << "L: Clearing connection @ "
                  << connections[i]->get_endpoint_name() << std::endl;
        delete(connections[i]);
        connections[i] = NULL;
    }
    number_of_server_connections = 0;
    connect_index = 0;

    log_file.close();
    return 0;
}

/* Util Function to check the correctness of the Functionality */
int kv739_get_spec(char * key, char * value, int c_index) {
    create_log_file();
    log_file.open(log_file_name, std::ofstream::app);

    /* Base Conditions */
    if(!is_connections_setup()) {
        log_file << "L: No connections available!" << std::endl;
        log_file.close();
        return -1;
    }
    if(key == NULL || value == NULL) {
        log_file << "L: Either key or value is NOT initialized!" << std::endl;
        log_file.close();
        return -1;
    }
    if(!verify_restrictions(key, 0)) {
        log_file << "L: Verification failed!" << std::endl;
        log_file.close();
        return -1;
    }
    log_file << "L: Sending GET(" << key << ") request to endpoint "
             << connections[c_index]->get_endpoint_name() << std::endl;

    /* Create Strings Key/Values */
    std::string key_request(key);
    std::string value_reply;

    //std::cout << "Connection state " << connections[c_index]->checkConnectionState() << std::endl;
    /* Send request to server */
    int response_code = connections[c_index]->get_request(key_request, value_reply);

    /* On Success */
    if(response_code == 0)
        std::copy(value_reply.begin(), value_reply.end(), value);

    log_file.close();
    return response_code;
}

int kv739_put_spec(char * key, char * value, char * old_value, int index) {
    create_log_file();
    log_file.open(log_file_name, std::ofstream::app);

    /* Base Conditions */
    if(!is_connections_setup()) {
        log_file << "L: No connections available!" << std::endl;
        log_file.close();
        return -1;
    }
    if(key == NULL || value == NULL) {
        log_file << "L: Either key or value is NOT initialized!" << std::endl;
        log_file.close();
        return -1;
    }
    if(!verify_restrictions(key, 0) || !verify_restrictions(value, 1)) {
        log_file << "L: Verification failed!" << std::endl;
        log_file.close();
        return -1;
    }

    /* Create Strings Key/Value/OldValue */
    std::string key_req(key);
    std::string value_req(value);
    std::string old_value_req;

    int response_code = 0;
    log_file << "L: Sending PUT(" << key << "," << value << ") request to endpoint "
             << connections[index]->get_endpoint_name() << std::endl;
    /* Send Request to server */
    response_code = connections[index]->put_request(key_req, value_req, old_value_req);

    /* On Success */
    if(response_code == 0)
        std::copy(old_value_req.begin(), old_value_req.end(), old_value);

    log_file.close();
    return response_code;
}

/* Util functions not exposed to the clients */
void list_connections() {
    std::cout << "L: List of server endpoints" << std::endl;
    for(int i = 0; i < number_of_server_connections; i++) {
        std::cout << "L : endpoint name : " << connections[i]->get_endpoint_name() << std::endl;
    }
}
void load_balancer() {
    connect_index = (connect_index + 1) % number_of_server_connections;
}
bool is_connections_setup() {
//    std::cout << number_of_server_connections << std::endl;
    if(number_of_server_connections == 0)
        return false;
    return true;
}
void create_log_file() {
    //std::cout << "Is log file created : " << log_file_created << std::endl;
    if(log_file_created != 0)
        return;
    log_file_created = 1;
    time_t t = std::time(0);
    long int now = static_cast<long int> (t);
    log_file_name = log_file_name + std::to_string(now);

    log_file.open(log_file_name, std::ofstream::app);
    log_file.close();
}

bool verify_restrictions(char *value, int index) {
    if(index == 0) {
        /* Length should not be > 128 bytes */
        /* Printable ASCII */
        /* Should not include [/] or any special characters */
        if(strlen(value) > MAX_LENGTH_OF_KEY || check_if_string_contains_non_printable_ascii(value)
                || check_if_string_contains_special_character(std::string(value))) {
            log_file << "L: Verification on key failed!" << std::endl;
            return false;
        }
        return true;
    } else {
        /* Length should not be > 2048 bytes */
        /* Printable ASCII */
        /* Should not include [/] or any special characters */
        if(strlen(value) > MAX_LENGTH_OF_VALUE || check_if_string_contains_non_printable_ascii(value)
                || check_if_string_contains_special_character(value)) {
            log_file << "L: Verification on value failed!" << std::endl;
            return false;
        }
        return true;
    }
}

bool check_if_string_contains_non_printable_ascii(char *s) {
    int len = strlen(s);
    int index = 0;
    while(index < len) {
        if(!isprint(s[index])) {
            log_file << "L: Contains non printable ASCII" << std::endl;
            return true;
        }
        index++;
    }

    return false;
}

bool check_if_string_contains_special_character(std::string s) {
    if(s.find_first_not_of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890_") != std::string::npos) {
        log_file << "L: Contains special characters!" << std::endl;
        return true;
    }
    return false;
}
