#include <stdio.h>
#include <unistd.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <time.h>
#include "../Library/ClientServerInterface.h"

/* Global Constants */
#define MAX_FILE_LENGTH 10000
#define MAX_NUMBER_OF_SERVERS 1000
#define MAX_LENGTH_OF_END_POINT_NAME 100
#define MAX_LENGTH_KEY 128
#define MAX_LENGTH_VALUE 2048

/* Global Variables */
int number_of_servers = 0;
char *server_list_file_name = (char *)"./server_list_temp.csv";
char *testing_commands_file_name = (char *)"./testing_commands.txt";

/* Server Details */
char **server_names;
char **server_address;
char **server_ports;
char **server_master_mark;
char **client_interested_server_endpoints;

typedef struct file_details {
    int number_of_lines;
    char *fileName;
    char **contents;
} file;

void allocate_memory();
void verify_functionality();
void list_server_details();
void read_server_contact_list(file *f);
char* get_value(char *line, int index);
void readFile(char *filename, file *f);
char* get_server_list_file_name(file *f);
void sentContinuousGetRequests(char *key, int count);
void print_commands(file *testing_file);
void execute_commands_in_test_file(file *testing_file);
void read_get_key(char *line, int index, char *key);
void read_put_key_value(char *line, int index, char *key, char *value);
void sendGetRequestsToAll(char *key);
char** get_n_servers(char **client_interested_server_endpoints, int n);
char** get_specific_servers(char **client_interested_server_endpoints, int index);

int main(int argc, char *argv[]) {
    if(argc < 3) {
        printf("Not enough arguments\n");
        printf("Usage : ./Client [C|F] [SERVER_LIST_FILE] [COMMANDS_FILE]\n");
        exit(0);
    }
    time_t current = time(0);
    printf("TS : %lu\n", current);
    allocate_memory();
    server_list_file_name = argv[2];

    file *server_list_file = (struct file_details *)malloc(sizeof(file));
    readFile(server_list_file_name, server_list_file);
    printf("Number of servers : %d\n", server_list_file->number_of_lines);
    read_server_contact_list(server_list_file);
    list_server_details();

    if(argv[1][0] == 'F') {
        if(argc != 4) {
            printf("Not enough arguments\n");
            printf("Usage : ./Client [C|F] [SERVER_LIST_FILE] [COMMANDS_FILE]\n");
            exit(0);
        }
        testing_commands_file_name = argv[3];
        file *testing_command_file = (struct file_details *) malloc(sizeof(file));
        readFile(testing_commands_file_name, testing_command_file);
        //print_commands(testing_command_file);
        time_t start = time(0);
        execute_commands_in_test_file(testing_command_file);
        time_t end = time(0);
        printf("Time Taken : %f miliseconds\n", difftime(end, start) * 1000);
    } else {
        verify_functionality();
    }
    return 0;
}

void verify_functionality() {
    char key[MAX_LENGTH_KEY];
    char value[MAX_LENGTH_VALUE];
    char old_value[MAX_LENGTH_VALUE];
    int ret = 0;
    int index = 0;

    /* Testing Edge Cases */
    char non_printable_ascii[MAX_LENGTH_VALUE];
    non_printable_ascii[0] = 0x1F;
    non_printable_ascii[1] = '\0';
    char large_key[MAX_LENGTH_KEY] = "1111111111111111111111111111111111111111111112222";
    char special_character_key[] = "test_value()";
    time_t start, end;
    double time_taken;

    while(1) {
        memset(key, '\0', sizeof(key));
        memset(value, '\0', sizeof(value));
        memset(old_value, '\0', sizeof(old_value));
        index = 0;
        printf("0->Exit() 1->Init() 2->Get() 3->Put() 4->GetSpecific() 5->Shutdown() 6->Cont.Gets() 7->Gets_from_all 8->PutSpecific()\n");
        int option = 0;
        scanf("%d", &option);

        switch(option) {
            case 1:
                printf("C: Init connection\n");
                start = time(0);
                ret = kv739_init(client_interested_server_endpoints);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f seconds\n", time_taken);
                printf("C: Return value : %d\n", ret);
                break;

            case 2:
                printf("Enter a KEY for GET request: ");
                scanf("%s", key);
                start = time(0);
                ret = kv739_get(key, value);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                printf("REQ : GET, %s\n", key);
                printf("REP : %s, %d\n", value, ret);
                break;

            case 3:
                printf("Enter the KEY for PUT request: ");
                scanf("%s", key);
                printf("Enter the VALUE for PUT request: ");
                scanf("%s", value);
                start = time(0);
                ret = kv739_put(key, value, old_value);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                printf("REQ : PUT, %s, %s\n", key, value);
                printf("REP : %s,%d\n", old_value, ret);
                break;

            case 4:
                printf("Enter a KEY for GET request: ");
                scanf("%s", key);
                printf("Enter a SERVER INDEX for GET request: ");
                scanf("%d", &index);
                start = time(0);
                ret = kv739_get_spec(key, value, index);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                printf("REQ : GET, %s\n", key);
                printf("REP : %s, %d\n", value, ret);
                break;

            case 5:
                printf("C: Shutting down");
                start = time(0);
                ret = kv739_shutdown();
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                printf("Return Value : %d\n", ret);
                break;

            case 6:
                printf("Enter a KEY for GET request: ");
                scanf("%s", key);
                printf("Enter a Number of GET request: ");
                scanf("%d", &index);
                start = time(0);
                sentContinuousGetRequests(key, index);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                break;

            case 7:
                printf("Enter a KEY for GET request: ");
                scanf("%s", key);
                start = time(0);
                sendGetRequestsToAll(key);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                break;

            case 8:
                printf("Enter the KEY for PUT request: ");
                scanf("%s", key);
                printf("Enter the VALUE for PUT request: ");
                scanf("%s", value);
                printf("Enter a SERVER INDEX for GET request: ");
                scanf("%d", &index);
                start = time(0);
                ret = kv739_put_spec(key, value, old_value, index);
                end = time(0);
                time_taken = difftime(end, start) * 1000;
                printf("TT: %f milliseconds\n", time_taken);
                printf("REQ : PUT, %s, %s\n", key, value);
                printf("REP : %s,%d\n", old_value, ret);
                break;

            default:
                break;
        }
        if(option < 1 || option > 8)
           break;
    }
}

void sentContinuousGetRequests(char *key, int count) {
    char value[MAX_LENGTH_VALUE];
    for(int i = 0; i < count; i++) {
        int ret = kv739_get(key, value);
        printf("GET, %s, %s, %d\n", key, value, ret);
    }
}

void sendGetRequestsToAll(char *key) {
    char value[MAX_LENGTH_VALUE];
    for(int i = 0; i < number_of_servers; i++) {
        memset(value, '\0', sizeof(value));
        int ret = kv739_get_spec(key, value, i);
        printf("%s : %s, %s, %d\n", client_interested_server_endpoints[i], key, value, ret);
    }
}

void read_server_contact_list(file *f) {

    for(int i = 0; i < f->number_of_lines; i++) {
        int len = strlen(f->contents[i]) + 2;
        char *line = (char *)malloc(len * sizeof(char));
        strcpy(line, f->contents[i]);
        char delimiter[] = ",";
        char *ptr = strtok(line, delimiter);
        int index = 0;
        while(ptr != NULL) {
            switch(index) {
                case 0:
                    server_names[number_of_servers] = ptr;
                    break;
                case 1:
                    server_address[number_of_servers] = ptr;
                    break;
                case 2:
                    server_ports[number_of_servers] = ptr;
                    break;
                case 3:
                    server_master_mark[number_of_servers] = ptr;
                    break;
            }

            index++;
            ptr = strtok(NULL, delimiter);
        }
        number_of_servers++;
    }

    /* Allocate 2D Array for server infos */
    client_interested_server_endpoints = (char **)malloc((number_of_servers+1) * sizeof(char *));
    /* Assign the Server Endpoints to the client_interested_server_endpoints */
    for(int i = 0; i < number_of_servers; i++) {
        char *endpoint = (char *)malloc(MAX_LENGTH_OF_END_POINT_NAME * sizeof(char));
        strcpy(endpoint, server_address[i]);
        strcat(endpoint, ":");
        strcat(endpoint, server_ports[i]);
        client_interested_server_endpoints[i] = endpoint;
    }
    /* Adding null termination */
    client_interested_server_endpoints[number_of_servers] = (char *)"\0";
}

/* Util Functions */
char* get_value(char *line, int index) {
    //printf("LINE : %s\n", line);
    char delim[] = ",";
    char *ptr = strtok(line, delim);

    int curr_index = 0;
    while(ptr != NULL) {
        //printf("'%s'\n", ptr);
        if(curr_index == index) {
            char *file_name = (char *)malloc(sizeof(char) * 20);
            strcpy(file_name, ptr);
            return file_name;
        }

        ptr = strtok(NULL, delim);
        curr_index++;
    }
    char *empty = (char *)malloc(sizeof(char)* 6);
    strcpy(empty, "empty");
    return empty;
}

void readFile(char *filename, file *f) {
    FILE *fp = fopen(filename, "r");
    size_t len;
    size_t read;
    char *line = NULL;

    if(fp == NULL){
        printf("Read of %s failed!", filename);
    }

    f->fileName = (char *)malloc((strlen(filename) + 1) * sizeof(char));
    strcpy(f->fileName, filename);

    int line_number = 0;
    f->contents = (char **)malloc(MAX_FILE_LENGTH * sizeof(char *));

    int end_of_file = 0;
    while (end_of_file != 1) {
        char *line = (char *)malloc(1000 * sizeof(char));
        memset(line, '\0', 100);
        int len = 0;
        char c;
        do {
            c = fgetc(fp);
           //printf("|%c|\n", c);
            if(c == EOF) {
                end_of_file = 1;
                break;
            }
            if(c == '\n' || c == '\r')
                break;

            line[len++] = (char)c;
        } while(true);
        line[len] = '\n';
       // printf("LINE : %s\n", line);
        f->contents[line_number] = (char *)malloc((len + 1) * sizeof(char));
        strcpy(f->contents[line_number], line);
        line_number++;
        //free(line);
        //printf("Line Number : %d\n", line_number);
        /* When file too long */
        if(line_number == MAX_FILE_LENGTH) {
            printf("FILE TOO LONG!");
            exit(EXIT_FAILURE);
        }

    }
    f->number_of_lines = line_number - 1;
}

void list_server_details() {
    printf("End points to connect\n");
    for(int i = 0; i < number_of_servers; i++) {
        printf("%s\n", client_interested_server_endpoints[i]);
    }
}

void allocate_memory() {
    server_names = (char **)malloc(MAX_NUMBER_OF_SERVERS * sizeof(char *));
    server_address = (char **)malloc(MAX_NUMBER_OF_SERVERS * sizeof(char *));
    server_ports = (char **)malloc(MAX_NUMBER_OF_SERVERS * sizeof(char *));
    server_master_mark = (char **)malloc(MAX_NUMBER_OF_SERVERS * sizeof(char *));
}

char* get_server_list_file_name(file *f) {
    char *server_file_name = get_value(f->contents[0], 1);
    return server_file_name;
}

void print_commands(file *testing_file) {
    printf("Commands are : \n");
    for(int i = 0;i < testing_file->number_of_lines; i++)
        printf("%s", testing_file->contents[i]);
    printf("\n");
}

void execute_commands_in_test_file(file *testing_file) {
    printf("Executing Commands!\n");
    int ret = 0;
    char key[MAX_LENGTH_KEY];
    char value[MAX_LENGTH_VALUE];
    char old_value[MAX_LENGTH_VALUE];
    time_t current = time(0);
    char *fileName = (char *)malloc(100 * sizeof(char));
    strcpy(fileName, "./output_");
    char str[256];
    sprintf(str, "%ld", (long)current);
    strcat(fileName, str);
    strcat(fileName, ".txt");
    printf("Output FN : %s\n", fileName);
    FILE *fp = fopen(fileName, "w");
    time_t start, end;
    double time_taken;
    time_t all_start = time(0);
    for(int i = 0;i < testing_file->number_of_lines - 1; i++) {
//        sleep(2);
        memset(key, '\0', sizeof(key));
        memset(value, '\0', sizeof(value));
        memset(old_value, '\0', sizeof(old_value));
        char *line = testing_file->contents[i];
        char command = line[0];
        if(command == 'G') {
            printf("R: ");
            read_get_key(line, 2, key);
            start = time(0);
            ret = kv739_get(key, value);
            end = time(0);
            time_taken = difftime(end, start) * 1000;
            printf("G,%s,%s,%d\n", key, value, ret);
            line[strlen(line) - 1] = '\0';
            fprintf(fp, "[G,%10s,%10s,%10d,%10s,%10f]\n", key, value, ret, "", time_taken);
        } else if(command == 'P') {
            printf("R: ");
            read_put_key_value(line, 2, key, value);
            start = time(0);
            ret = kv739_put(key, value, old_value);
            end = time(0);
            time_taken = difftime(end, start) * 1000;
            printf("P,%s,%s,%s,%d\n", key, value, old_value, ret);
            line[strlen(line) - 1] = '\0';
            fprintf(fp, "[P,%10s,%10s,%10s,%10d,%10f]\n", key, value, old_value, ret, time_taken);
        } else if(command == 'I') {
            printf("R: ");
            start = time(0);
            ret = kv739_init(client_interested_server_endpoints);
            end = time(0);
            time_taken = difftime(end, start) * 1000;
            printf("I,%d\n", ret);
            line[strlen(line) - 1] = '\0';
            fprintf(fp, "[I,%10s,%10s,%10s,%10d,%f]\n", "","","",ret,time_taken);
        } else if(command == 'S') {
            printf("R: ");
            start = time(0);
            ret = kv739_shutdown();
            end = time(0);
            time_taken = difftime(end, start) * 1000;
            printf("S,%d\n", ret);

            line[strlen(line) - 1] = '\0';
            fprintf(fp, "[S,%10s,%10s,%10s,%10d,%10f]\n", "","","",ret,time_taken);
        } else if(command == 'J') {
            printf("R: ");
            read_get_key(line, 2, key);
            start = time(0);
            ret = kv739_init(get_n_servers(client_interested_server_endpoints, atoi(key)));
            end = time(0);
            time_taken = difftime(end, start) * 1000;
            fprintf(fp, "[I,%10s,%10s,%10s,%10d,%10f]\n", "","","",ret,time_taken);
            line[strlen(line) - 1] = '\0';
            printf("I,%d,%d\n", atoi(key),ret);
        } else if(command ==  'K') {
            printf("R: ");
            read_get_key(line, 2, key);
            start = time(0);
            ret = kv739_init(get_specific_servers(client_interested_server_endpoints, atoi(key)));
            end = time(0);
            time_taken = difftime(end, start) * 1000;
            printf("I,%d\n", ret);
            line[strlen(line) - 1] = '\0';
            fprintf(fp, "[I,%10d,%10s,%10s,%10s,%10f]\n",ret,"","","",time_taken);
        }
        else {
            printf("UNKNOWN COMMAND\n");
            fprintf(fp, "UNKNOWN COMMAND\n");
            break;
        }
    }
    time_t all_end = time(0);
    printf("TTK : %f milliseconds\n", difftime(all_end, all_start) * 1000 );
    fclose(fp);
}

void read_get_key(char *line, int index, char *key) {
    int i = index;
    int k = 0;
    while(line[i] != '\n') {
        key[k] = line[i]; i++; k++;
    }
    key[k] = '\0';
}

void read_put_key_value(char *line, int index, char *key, char *value) {
    int i = index;
    int k = 0;
    while(line[i] != ',') {
        key[k] = line[i]; i++; k++;
    }
    key[k] = '\0';

    k = 0; i++;
    while(line[i] != '\n') {
        value[k] = line[i]; i++; k++;
    }
    value[k] = '\0';
}

char** get_n_servers(char **client_interested_server_endpoints, int n) {
    char **current_interested_servers = (char **)malloc(sizeof(char *) * (n+1));
    for(int i = 0; i < n; i++) {
        current_interested_servers[i] = (char *)malloc(sizeof(char) * (strlen(client_interested_server_endpoints[i]) + 1));
        strcpy(current_interested_servers[i], client_interested_server_endpoints[i]);
    }
    current_interested_servers[n] = (char *)malloc(sizeof(char) * MAX_LENGTH_OF_END_POINT_NAME);
    strcpy(current_interested_servers[n], "\0");
    return current_interested_servers;
}

char** get_specific_servers(char **client_interested_server_endpoints, int index) {
    char **current_interested_servers = (char **)malloc(sizeof(char *) * (2));

    current_interested_servers[0] = (char *)malloc(sizeof(char) * (strlen(client_interested_server_endpoints[index]) + 1));
    strcpy(current_interested_servers[0], client_interested_server_endpoints[index]);
    current_interested_servers[1] = (char *)malloc(sizeof(char) * MAX_LENGTH_OF_END_POINT_NAME);
    current_interested_servers[1] = (char *)"\0";
    return current_interested_servers;
}
