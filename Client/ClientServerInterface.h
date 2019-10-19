#ifndef CLIENT_SERVER_INTERFACE_H_
#define CLIENT_SERVER_INTERFACE_H_

int kv739_get(char * key, char * value);
int kv739_put(char * key, char * value, char * old_value);

#endif
