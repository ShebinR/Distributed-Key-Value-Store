Steps to use Shared Library in application
------------------------------------------
1. Build and create application refrencing shared library

gcc -L<Path_Where_.so_file_is> -Wall -o <application_name> <application_source_file> -l<library_name_stripped>

2. While running use the following command

a. Copy the .so file locally and run application

