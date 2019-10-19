Steps to create shared library
-------------------------------
1. Create object file
g++ -c -Wall -Werror -fpic <source_file_name>

2. Create shared library

g++ -shared -o <library_name>.so <object_file_name>.o
