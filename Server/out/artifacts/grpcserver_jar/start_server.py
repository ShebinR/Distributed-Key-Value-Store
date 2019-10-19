import subprocess
import sys
import time

server_ids = []
server_address = []
server_ports = []
server_identity = []

def get_pid_of_by_port(port_number):
    get_port_number_cmd = ["lsof", "-t", "-i"]
    get_port_number_cmd.append("TCP:" + str(port_number))
    p = subprocess.Popen(get_port_number_cmd, stdout=subprocess.PIPE)
    out = p.communicate()
    if out[0].rstrip() != "":
        print("PID of processing running @ " + str(port_number) + " : " + out[0])
    return out[0].rstrip()

def get_pid_of_by_name():
    get_port_number_cmd = ["ps", "aux", "|", "grep", "grpcserver", "awk", "'{print $2}'"]
    print(get_port_number_cmd)
    p = subprocess.Popen(get_port_number_cmd, stdout=subprocess.PIPE)
    out = p.communicate()
    if out[0].rstrip() != "":
        print("PID of processing running @ " + str(port_number) + " : " + out[0])
    return out[0].rstrip()

def kill_process_with_pid(pid):
    kill_process_command = ["kill", "-9"]
    kill_process_command.append(str(pid))
    print("Killing process with PID : " + pid)
    p = subprocess.Popen(kill_process_command, stdout=subprocess.PIPE)
    out = p.communicate()
    
def clear_old_processes():
    PIDs = []
    for port_number in server_ports:
        print(port_number)
        PID = get_pid_of_by_port(port_number)
        if PID != "":
            PIDs.append(PID);
    for pid in PIDs:
        kill_process_with_pid(pid)

def read_server_list_file(config_file):
    with open(config_file, "r") as fp:
        for index, line in enumerate(fp):
            fields = line.rstrip().split(",")
            server_ids.append(fields[0]);
            server_address.append(fields[1])
            server_ports.append(fields[2])
            server_identity.append(fields[3])

def run_server_process(n, config_file):
    for i in range(min(n, len(server_ids))):
        print("Starting server {} with address {} at {} assuming {}".format(server_ids[i], server_address[i], server_ports[i], server_identity[i]))
        subprocess.Popen(["java", "-jar", "./grpcserver.jar", server_ids[i], server_address[i], server_ports[i], server_identity[i], config_file])
        time.sleep(2)

def main():
    if len(sys.argv) != 2:
        print("usage: python ./start_server.py <config_file>")
        return
    config_file = sys.argv[1]
    read_server_list_file(config_file)
    print(server_ports)
    clear_old_processes()
    run_server_process(5, config_file)

main()
#subprocess.Popen(["java", "-jar", "./grpcserver.jar", "S4", "localhost", "9090", "0"])
