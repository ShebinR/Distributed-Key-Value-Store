start_port_number = 15555
for i in  range(100):
    print "S" + str(i) + ",localhost," + str(start_port_number + i) + ",0"
