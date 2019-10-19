package utils;


import service.ServerProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MethodUtils {

    /**
     * This method parses the csv config file and creates the list of server properties
     * @param path - path of the csv server config file
     * @return - list of server properties objects
     */
    public static List<ServerProperties> parseServerConfig(String path){
        List<ServerProperties> serverList = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            serverList = new ArrayList<>();

            String line;
            ServerProperties sObj;
            // CSV file format : host, port, getIsMaster
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] serverProperties = line.split(",");
                if(serverProperties.length < 4){
                    //Error in config file format or the parsed row
                    System.out.println("Server properties length error");
                    continue;
                }
                sObj = new ServerProperties(serverProperties[0], serverProperties[1],
                        Integer.parseInt(serverProperties[2]), Integer.parseInt(serverProperties[3]) == 1
                );
               // sObj.printServerProperties();
                serverList.add(sObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
            serverList = null;
        }
        return serverList;
    }

    /**
     * Checks if the server config passed as arguments is valid and is found in the config file
     * @param serverList - List of servers in the service system
     * @param server - current server config passed as argument
     * @return
     */
    public static boolean isValidServer(List<ServerProperties> serverList, ServerProperties server){
        for(ServerProperties obj : serverList){
            if(!obj.getServerId().equals(server.getServerId())){
                continue;
            }
            //System.out.println("Found");
            //obj.printServerProperties();
            //server.printServerProperties();
            if(obj.getPort() == server.getPort() &&
                    obj.getHost().equals(server.getHost()) &&
                    obj.getIsMaster() == server.getIsMaster()){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    /**
     * Method to find the master server from the list of servers
     * @param serverList - List of server parsed from config file
     * @return - index of the master in the masterList, if found else -1
     */
    public static int findMaster(List<ServerProperties> serverList){
        for(int i=0; i< serverList.size(); i++){
            if(serverList.get(i).getIsMaster()){
                return i;
            }
        }
        return -1;
    }


    /**
     * Finds the index of the server in the serverList with given hostname and port number
     * @param serverList - List of servers to search from
     * @param host - hostname
     * @param port - port number
     * @return Index on success; -1 otherwise
     */
    public static int findServerIndexFromAddress(List<ServerProperties> serverList, String host, int port){
        for(int i=0; i< serverList.size(); i++){
            if(serverList.get(i).getHost().equals(host) && serverList.get(i).getPort() == port){
                return i;
            }
        }
        return -1;
    }

    public static void updateMasterServer(List<ServerProperties> serverProperties, String masterServerId) {
        //System.out.println(" Master ID " + masterServerId);
        for (int i = 0; i < serverProperties.size(); i++) {
            if (serverProperties.get(i).getServerId().equals(masterServerId)) {
                serverProperties.get(i).setIsMaster(true);
                continue;
            }
            serverProperties.get(i).setIsMaster(false);
        }
    }

    /**
     * Finds the index of the server in the serverList with given serverId
     * @param serverList - List of servers to search from
     * @param serverId - serverId
     * @return Index on success; -1 otherwise
     */
    public static int findServerIndexFromServerId(List<ServerProperties> serverList, String serverId){
        for(int i=0; i< serverList.size(); i++){
            if(serverList.get(i).getServerId().equals(serverId)){
                return i;
            }
        }
        return -1;
    }

}
