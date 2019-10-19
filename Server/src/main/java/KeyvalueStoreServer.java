import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.KeyValueStoreServerImpl;
import service.ServerProperties;
import utils.Constants;
import utils.MethodUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KeyvalueStoreServer {

    private static Server server = null;
    private static KeyValueStoreServerImpl service = null;
    public static void main(String[] args) throws IOException, InterruptedException {
        if(args == null || args.length < 5){
            System.out.println("Error: Invalid arguments");
            System.out.println("usage: java -jar <service_name> " +
                    "<server_id> <server address> <port> <is_master> <config_file>");
            return;
        }
        String serverId = args[0];
        int port = Integer.parseInt(args[2]);
        String configFileName = args[4];

        /* Server Log creation */
        createServerLog(serverId);

        /* Attaching Shutdown */
        addShutdownHooks();

        System.out.println("Starting Server...");
        List<ServerProperties> serverList = MethodUtils.parseServerConfig(configFileName);
        if(serverList == null){
            //Config file parse error
            System.out.println("Unable to parse server config..");
            return;
        }
        ServerProperties serverObj = new ServerProperties(args[0], args[1], Integer.parseInt(args[2]),
                Integer.parseInt(args[3]) == 1);

        if(!MethodUtils.isValidServer(serverList, serverObj)){
            //No server found with the given config
            System.out.println("No server found with the given config");
            return;
        }
        int masterIndex = MethodUtils.findMaster(serverList);
        //System.out.println("Master Index "+ masterIndex);
        if(masterIndex == Constants.OPERATION_FAILURE_STATUS_CODE){
            //Master not found, mark update as failure
            System.out.println("master not found in config ");
        }
        long s_time = System.currentTimeMillis();
        service = new KeyValueStoreServerImpl(serverObj, serverList, masterIndex);
        server = ServerBuilder.forPort(port).addService(service).build();
        server.start();
        long e_time = System.currentTimeMillis();
        System.out.println("Time taken for Starting service : " + (e_time - s_time) + " milliseconds");

        System.out.printf("[Node ID : %s] [Master : %s] [listening @ %d]\n",
                serverObj.getServerId(), serverObj.getIsMaster(), serverObj.getPort());
        System.out.println();
        try{
            server.awaitTermination();
        }finally{
            System.out.println("Terminated...");
            server.shutdownNow();
        }
    }

    private static void addShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nInside Shutdown Hook");
            //service.getCommunication();
            if(service != null)
                service.closeAllCommunication();
            if(server != null)
                server.shutdownNow();
            System.out.println("Terminating the server!");
        }));
        System.out.println("Shutdown Hook Attached for handling killing!");
    }

    private static void createServerLog(String serverId) throws FileNotFoundException {
        //Redirect the console outputs to a file
        String fileName = "./logs/log-" + serverId + ".txt";
        PrintStream outStream = new PrintStream(new FileOutputStream(fileName, true));
        System.setOut(outStream);

        //Writing the date and time to start with
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("\n****************************************");
        System.out.println(dateFormat.format(date));
        System.out.println("****************************************\n");
    }
}
