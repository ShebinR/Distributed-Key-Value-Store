#ifndef CLIENT_SERVER_INTERFACE_H_
#define CLIENT_SERVER_INTERFACE_H_

int kv739_get(char * key, char * value);
int kv739_put(char * key, char * value, char * old_value);
int kv739_put_spec(char * key, char * value, char * old_value, int index);
int kv739_init(char ** server_list);
int kv739_get_spec(char * key, char * value, int c_index);
int kv739_shutdown();

#endif
