package service;

import com.grpc.protocol.Keyvaluestore;
import com.grpc.protocol.keyvaluestoreGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import utils.Constants;
import utils.DBConnection;
import utils.MethodUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class Communication {
    ServerProperties nodeProperty;
    int masterIndex;
    List<ServerProperties> serverList;
    HashMap<String, CommunicationStub> communicationStubHashMap;
    HashMap<String, CommunicationStub> communicationStubHashMapTemp;
    DBConnection dbConnection;

    Communication(List<ServerProperties> servers, boolean isMaster, ServerProperties nodeProperty, DBConnection db) {
        this.serverList = servers;
        this.communicationStubHashMap = new HashMap<>();
        this.communicationStubHashMapTemp = new HashMap<>();

        this.nodeProperty = nodeProperty;
        this.dbConnection = db;

        System.out.println("Static Service Network Config");
        System.out.println("------------------------------");
        for (ServerProperties server : serverList) {
            server.printServerProperties();
        }

        long time_s = System.currentTimeMillis();
        masterDiscoveryProtocol();
        long time_e = System.currentTimeMillis();
        System.out.println("Time taken for MDP : " + (time_e - time_s) + " milli seconds");

        System.out.println("Updated Service Network Config");
        System.out.println("------------------------------");
        for (ServerProperties server : serverList) {
            server.printServerProperties();
        }

        /* Run DB Consistency Check Protocol */
        System.out.println();
        runDBConsistencyCheckProtocol();
        System.out.println();

        /* Create Communication Channels */
        createRequiredCommunicationChannels(nodeProperty.getIsMaster());
        System.out.println();
        System.out.println("Communication Stub status : ");
        printCommnicationStubStatus();
        System.out.println("Communication Stub Temp status : ");
        printCommnicationStubTempStatus();
    }

    void createRequiredCommunicationChannels(boolean isMaster){
        if(isMaster) {
            for (ServerProperties server : serverList) {
                if(server.getIsMaster()){
                    continue;
                }
                if(communicationStubHashMap.containsKey(server.getServerId())) {
                    System.out.println("Communication stub already available!");
                    continue;
                }
                communicationStubHashMap.put(server.getServerId(), new CommunicationStub(server.getServerId(), server));
            }
        } else {
            int masterIndex = MethodUtils.findMaster(serverList);
            communicationStubHashMap.put(serverList.get(masterIndex).getServerId(), new CommunicationStub(
                    serverList.get(masterIndex).getServerId(), serverList.get(masterIndex)
            ));
        }
    }

    void printCommnicationStubStatus() {
        for(Map.Entry<String, CommunicationStub> entry: communicationStubHashMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().communicationStub + " : " +
                    entry.getValue().isAlive);
        }
    }


    void printCommnicationStubTempStatus() {
        for(Map.Entry<String, CommunicationStub> entry: communicationStubHashMapTemp.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().communicationStub + " : " +
                    entry.getValue().isAlive);
        }
    }

    void masterDiscoveryProtocol() {
        System.out.println();
        System.out.println("Running master discovery protocol");
        // As per config file
        if (nodeProperty.getIsMaster()) {
            List<Keyvaluestore.WhoIsMasterResponse> responses = askWhoIsMasterToAllNodesExceptSelf();
            if (responses.size() == 0) {
//                System.out.println("No Other hosts.. Making myself as master");
                MethodUtils.updateMasterServer(serverList, this.nodeProperty.getServerId());
            } else {
//                System.out.println("Found some replies");
                if (checkMisMatchInWhoIsMasterReplies(responses)) {
                    System.out.println("WARN: Observed mismatch in master verification!");
                    /* TBD : How to handle it? */
                }
                // Update my config to new master
                MethodUtils.updateMasterServer(serverList, responses.get(0).getServerId());

                // Change my isMaster to false
                this.nodeProperty.setIsMaster(false);
            }
        } else {
            int masterIndex = MethodUtils.findMaster(serverList);
            ServerProperties masterNode = serverList.get(masterIndex);
            boolean areYouMaster = areYouMaster(masterNode);

            if (!areYouMaster) {
                List<Keyvaluestore.WhoIsMasterResponse> responses = askWhoIsMasterToAllNodesExceptSelf();
                if (responses.size() == 0) {
                    MethodUtils.updateMasterServer(serverList, this.nodeProperty.getServerId());
                } else {
                    if (checkMisMatchInWhoIsMasterReplies(responses)) {
                        System.out.println("WARN: Observed mismatch in master verification!");
                        /* TBD : How to handle it? */
                    }
                    // Update my config to new master
                    MethodUtils.updateMasterServer(serverList, responses.get(0).getServerId());
                }
            }
        }
        this.masterIndex = MethodUtils.findMaster(serverList);
        // Update my nodeProperty
        if(serverList.get(masterIndex).getServerId().equals(nodeProperty.getServerId()))
            this.nodeProperty.setIsMaster(true);

        System.out.println("Master discovery protocol completed!");
        System.out.println();
    }

    Keyvaluestore.WhoIsMasterResponse askWhoIsMaster(ServerProperties server) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                .usePlaintext().build();
        Keyvaluestore.EmptyRequest request = Keyvaluestore.EmptyRequest.newBuilder()
                .build();
        keyvaluestoreGrpc.keyvaluestoreBlockingStub requestStub = keyvaluestoreGrpc.newBlockingStub(channel);
        Keyvaluestore.WhoIsMasterResponse response = null;
        try {
            response = requestStub.whoIsMaster(request);
        } catch (Exception e) {
            System.out.println(server.getServerId() + " : " + e.getMessage());
        }

        // Channel Shutdown: To Test
        channel.shutdownNow();
        return response;
    }

    List<Keyvaluestore.WhoIsMasterResponse> askWhoIsMasterToAllNodesExceptSelf() {
        List<Keyvaluestore.WhoIsMasterResponse> responses = new ArrayList<>();
        /* Do a blocking request to each alive server */

//        for (int i = 0; i < serverList.size(); i++) {
//            if (serverList.get(i).getServerId().equals(nodeProperty.getServerId())) {
//                System.out.println(nodeProperty.getServerId() + " : Skipping");
//                continue;
//            }
//            Keyvaluestore.WhoIsMasterResponse reply = askWhoIsMaster(serverList.get(i));
//            if (reply != null)
//                responses.add(reply);
//        }
        List<ManagedChannel> channels = new ArrayList<>();
        final CountDownLatch finishLatch = new CountDownLatch(serverList.size());
        for(ServerProperties server : serverList){
            if(server.getIsMaster()){
                finishLatch.countDown();
                System.out.println("Master : Ignoring Countdown updated "+ finishLatch.getCount());
                continue;
            }
            // Previous updates would have failed!
            ManagedChannel channel = ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                    .usePlaintext().build();
            channels.add(channel);

            keyvaluestoreGrpc.keyvaluestoreStub requestStub = keyvaluestoreGrpc.newStub(channel);
            communicationStubHashMap.put(server.getServerId(), new CommunicationStub(channel, requestStub, server.getServerId()));
            Keyvaluestore.EmptyRequest request = Keyvaluestore.EmptyRequest.newBuilder()
                    .build();
            requestStub.withDeadlineAfter(60, TimeUnit.SECONDS).whoIsMaster(request, new StreamObserver<Keyvaluestore.WhoIsMasterResponse>() {
                @Override
                public void onNext(Keyvaluestore.WhoIsMasterResponse response) {
                    responses.add(response);
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println(t.getMessage());
                    finishLatch.countDown();
                    System.out.println("Error : countdown updated "+ finishLatch.getCount());
                }

                @Override
                public void onCompleted() {
                    finishLatch.countDown();
                    System.out.println("Success : countdown updated "+ finishLatch.getCount());
                }
            });
        }
        try {
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Completed non-blocking call");
        }
        /*for(ManagedChannel c: channels)
            c.shutdownNow();*/

        return responses;
    }

    boolean areYouMaster(ServerProperties masterServer) {
        /* Creating Manged Channel since we are using it only once & shutting down */
        /* All communication based channels are persisted */
        ManagedChannel channel = ManagedChannelBuilder.forAddress(masterServer.getHost(), masterServer.getPort())
                .usePlaintext().build();
        Keyvaluestore.EmptyRequest request = Keyvaluestore.EmptyRequest.newBuilder()
                .build();
        keyvaluestoreGrpc.keyvaluestoreBlockingStub requestStub = keyvaluestoreGrpc.newBlockingStub(channel);
        Keyvaluestore.AreYouMasterResponse response = null;
        boolean areYouMaster = false;
        try {
            response = requestStub.areYouMaster(request);
        } catch (Exception e) {
            System.out.println("KNOWN MASTER " + masterServer.getServerId() + " : " + e.getMessage());
            return areYouMaster;
        }
        areYouMaster = response.getResponse();

        // Channel Shutdown: To Test
        channel.shutdownNow();

        return areYouMaster;
    }

    boolean checkMisMatchInWhoIsMasterReplies(List<Keyvaluestore.WhoIsMasterResponse> responses) {
        String possibleMasterNode = responses.get(0).getServerId();
        for (int i = 1; i < responses.size(); i++) {
            if (!possibleMasterNode.equals(responses.get(i).getServerId())) {
                return true;
            }
        }
        return false;
    }

    void runDBConsistencyCheckProtocol() {
        System.out.println("Running DB Consistency Check Protocol");
        /* If Master */
        if(nodeProperty.getIsMaster()) {
            System.out.println("Master: Data should be consistent!");
            System.out.println("DB Consistency Check Protocol Completed");
            return;
        }

        /* Find Last Timestamp of the records from DB */
        long lastKnownTS = dbConnection.getLastTimeStamp();

        /* Send Request for UpdatesAfterLastKnownTS fo the record */
        /* Receive records from Master */
        ServerProperties masterNode = serverList.get(MethodUtils.findMaster(serverList));
        List<String> requiredUpdates = askUpdatesAfterTSFromMaster(lastKnownTS, masterNode);

        if(requiredUpdates.size() != 0) {
            /* Update DB */
            int ret_code = dbConnection.applyUpdatesFromMaster(requiredUpdates);
            if (ret_code == Constants.OPERATION_FAILURE_STATUS_CODE) {
                /* Stop from starting */
                System.out.println("ERROR: Could not make DB consistent! Exiting!");
                System.exit(0);
            }
        }
        System.out.println("DB Consistency Check Protocol Completed");
    }

    List<String> askUpdatesAfterTSFromMaster(long lastKnowTS, ServerProperties masterNode) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(masterNode.getHost(), masterNode.getPort())
                .usePlaintext().build();
        Keyvaluestore.FindUpdatesAfterTSRequest request = Keyvaluestore.FindUpdatesAfterTSRequest.newBuilder()
                .setLastKnownTimeStamp(lastKnowTS)
                .setServerId(nodeProperty.getServerId())
                .build();
        keyvaluestoreGrpc.keyvaluestoreBlockingStub requestStub = keyvaluestoreGrpc.newBlockingStub(channel);
        Keyvaluestore.UpdatesAfterTSResponse response = requestStub.getUpdatesAfterTS(request);
        channel.shutdownNow();
        if(response.getNoUpdates()) {
            return new ArrayList<>();
        } else {
            String records[] = response.getRecords().split("\\n");
            return new ArrayList<>(Arrays.asList(records));
        }
    }

    /**
     * Method to create a communication channel to other server and pass the updates through a put request
     *
     * @param key   - The key to update in db
     * @param value -  the value for the key
     */
     int propagateUpdate(String key, String value, long timeStamp) {
         int[] respCode = {0};
         final CountDownLatch finishLatch = new CountDownLatch(serverList.size());
         List<String> notAlive = new ArrayList<>();
         List<String> gotResponse = new ArrayList<>();
         List<String> errorResponse = new ArrayList<>();
         for(ServerProperties server : serverList){
             if(server.getIsMaster()){
                 finishLatch.countDown();
                 System.out.println("I am the Master : countdown updated "+ finishLatch.getCount());
                 continue;
             }

             // Changes done : Reuse stubs

             // Previous updates would have failed!
             CommunicationStub comm = communicationStubHashMap.get(server.getServerId());
             if(!comm.isAlive){
                 finishLatch.countDown();
                 notAlive.add("[" + server.getServerId() + "-1]");
                 //System.out.println("Not alive : countdown updated "+ finishLatch.getCount());
                 continue;
             }
             keyvaluestoreGrpc.keyvaluestoreStub propagateStub = comm.communicationStub;
             Keyvaluestore.UpdateFromMasterRequest updateRequest = Keyvaluestore.UpdateFromMasterRequest.newBuilder()
                     .setKey(key)
                     .setValue(value)
                     .setTimeStamp(timeStamp)
                     .build();

             System.out.println("Sending updates to "+ server.getServerId());

             //Call put api of the server

             propagateStub.withDeadlineAfter(60, TimeUnit.SECONDS).updateFromMaster(updateRequest, new StreamObserver<Keyvaluestore.DefaultResponse>() {
                 @Override
                 public void onNext(Keyvaluestore.DefaultResponse value) {
                     gotResponse.add("[" + server.getServerId() + ":" + value.getResponseCode() + "]");
                     //System.out.println("Got response from " + server.getServerId() + "with code "+ value.getResponseCode());
                     respCode[0] = value.getResponseCode();
                 }

                 @Override
                 public void onError(Throwable t) {
                     System.out.println(t.getMessage());
                     // Is this the right place?
                     comm.isAlive = false;
                     finishLatch.countDown();
                     errorResponse.add("[" + server.getServerId() + ":" + finishLatch.getCount() + "]");
                     //System.out.println("Error : countdown updated "+ finishLatch.getCount());
                 }

                 @Override
                 public void onCompleted() {
                     finishLatch.countDown();
                     System.out.println("Success : countdown updated "+ finishLatch.getCount());
                 }
             });

             //If any one of the update to other server fails, then break and report error.
             // Also, set its communication channel to be dead so that it doesn't prevent subsequent
             // operations
             if(respCode[0] == Constants.OPERATION_FAILURE_STATUS_CODE){
                 comm.isAlive = false;
                 while(finishLatch.getCount() > 0){
                     finishLatch.countDown();
                 }
                 break;
             }
        }
         try {
             finishLatch.await(1, TimeUnit.MINUTES);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        System.out.println("Exiting propagate update function ");
         System.out.println("List of servers Not alive : ");
         System.out.println(notAlive);
         System.out.println("List of servers which responded");
         System.out.println(gotResponse);
         System.out.println("List of servers with errors");
         System.out.println(errorResponse);

        return respCode[0];
    }

    /**
     * Method to send put request to the master from a non-master server instance.
     *
     * @param key   - They key to upadte
     * @param value - The value to update
     * @return - Response or error code of the operation
     */
    int sendUpdateToMaster(String key, String value, int masterIndex) {
        int[] respCode = {0};

         ServerProperties master = serverList.get(masterIndex);
        System.out.println("Master Server : " + master);
         // CHange to use existing stub
         CommunicationStub comm = communicationStubHashMap.get(master.getServerId());
         keyvaluestoreGrpc.keyvaluestoreStub propagateStub = comm.communicationStub;
        Keyvaluestore.PropogatePutRequest request = Keyvaluestore.PropogatePutRequest.newBuilder()
                .setKey(key)
                .setValue(value)
                .build();
        System.out.println("Sending put update to master ");
        final CountDownLatch finishLatch = new CountDownLatch(1);

        propagateStub.propogatePut(request, new StreamObserver<Keyvaluestore.PutResponse>() {
            @Override
            public void onNext(Keyvaluestore.PutResponse value) {
                System.out.println("Got response from master with code " + value.getResponseCode());
                respCode[0] = value.getResponseCode();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
                respCode[0] = Constants.SERVER_UNAVAILABLE_STATUS_CODE;
                comm.isAlive = false;
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        });
        try {
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting send update to master function ");

        return respCode[0];
    }

    /**
     * Broadcast revert update to other servers
     *
     * @param key
     * @param value
     * @param isNew
     * @return
     */
    int propagateRevert(String key, String value, boolean isNew) {
        int[] respCode = {0};
        final CountDownLatch finishLatch = new CountDownLatch(serverList.size());

        for (ServerProperties server : serverList) {
            if (server.getIsMaster()) {
                finishLatch.countDown();
                continue;
            }
            CommunicationStub comm = communicationStubHashMap.get(server.getServerId());
            if(!comm.isAlive)
            {
                finishLatch.countDown();
                continue;
            }

            keyvaluestoreGrpc.keyvaluestoreStub propagateStub = comm.communicationStub;

            Keyvaluestore.RevertRequest updateRequest = Keyvaluestore.RevertRequest.newBuilder()
                    .setKey(key)
                    .setOldValue(value)
                    .setIsNew(isNew)
                    .build();

            System.out.println("Sending revert updates to "+ server.getServerId());

            //Call put api of the server
            propagateStub.withDeadlineAfter(60, TimeUnit.SECONDS)
                    .revertUpdate(updateRequest, new StreamObserver<Keyvaluestore.DefaultResponse>() {
                @Override
                public void onNext(Keyvaluestore.DefaultResponse value) {
                    System.out.println("Got response from " + server.getServerId() + "with code "+ value.getResponseCode());
                    respCode[0] = Math.min(respCode[0], value.getResponseCode());
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println(t.getMessage());
                    comm.isAlive = false;
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    finishLatch.countDown();
                }
            });

        }
        try {
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting propagate revert function ");

        return respCode[0];
    }

     /**
      * Method to inform the new master candidate of its election
      * @param newMasterIndex - The index of the master server properties in the server list
      * @return - 0 -succes ,-1 - operation failure and -2 - server unavailable
      */
     int informNewMaster(int newMasterIndex){

         int[] respCode = {0};
         ServerProperties server = serverList.get(newMasterIndex);

         //There is no channel yet to communicate with this non-master server.
         // create one and add it to the hashmap
         if(!communicationStubHashMap.containsKey(server.getServerId())){
             communicationStubHashMap.put(server.getServerId(), new CommunicationStub(server.getServerId(), server));
         }
         CommunicationStub comm = communicationStubHashMap.get(server.getServerId());
         //If master channel is broken, then return unavailable, so that the current server instance
         // will choose another new master candidate
         if(!comm.isAlive){
             respCode[0] = Constants.SERVER_UNAVAILABLE_STATUS_CODE;
             return respCode[0];
         }

         keyvaluestoreGrpc.keyvaluestoreStub propagateStub = comm.communicationStub;

        final CountDownLatch finishLatch = new CountDownLatch(1);
         Keyvaluestore.AcceptNewMasterRoleRequest updateRequest = Keyvaluestore.AcceptNewMasterRoleRequest.newBuilder()
                 .build();

         System.out.println("Sending informNewMaster update to "+ server.getServerId());

         propagateStub.acceptNewMasterRole(updateRequest, new StreamObserver<Keyvaluestore.DefaultResponse>() {
             @Override
             public void onNext(Keyvaluestore.DefaultResponse value) {
                 System.out.println("Got response from " + server.getServerId() + "with code "+ value.getResponseCode());
                 respCode[0] = value.getResponseCode();
             }

             @Override
             public void onError(Throwable t) {
                 System.out.println(t.getMessage());
                 respCode[0] = Constants.SERVER_UNAVAILABLE_STATUS_CODE;
                 comm.isAlive = false;
                 finishLatch.countDown();
             }

             @Override
             public void onCompleted() {
                 finishLatch.countDown();
             }
         });

        try {
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return respCode[0];
    }

     /**
     * Method to propogate the change of master to other servers
     * @param hostname - The hostname of the new master
     * @param port - The port of the new master
     * @return - 0-success, -1-failure
     */
    int propagateMasterChange(String hostname, int port) {
        int[] respCode = {0};
        final CountDownLatch finishLatch = new CountDownLatch(serverList.size());

        for (ServerProperties server : serverList) {
            if (server.getIsMaster()) {
                finishLatch.countDown();
                continue;
            }
            CommunicationStub comm = communicationStubHashMap.get(server.getServerId());
            if(!comm.isAlive)
            {
                finishLatch.countDown();
                continue;
            }

            keyvaluestoreGrpc.keyvaluestoreStub propagateStub = comm.communicationStub;


            Keyvaluestore.ChangeMasterRequest updateRequest = Keyvaluestore.ChangeMasterRequest.newBuilder()
                    .setHostname(hostname)
                    .setPort(port)
                    .build();

            System.out.println("Sending master change updates to "+ server.getServerId());

            //Call put api of the server
            propagateStub.withDeadlineAfter(60, TimeUnit.SECONDS).
                    changeMaster(updateRequest, new StreamObserver<Keyvaluestore.DefaultResponse>() {
                @Override
                public void onNext(Keyvaluestore.DefaultResponse value) {
                    System.out.println("Got response from " + server.getServerId() + "with code "+ value.getResponseCode());
                    //respCode[0] = value.getResponseCode();
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println(t.getMessage());
                    comm.isAlive = false;
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    finishLatch.countDown();
                }
            });

//            if(respCode[0] == Constants.OPERATION_FAILURE_STATUS_CODE){
//                comm.isAlive = false;
//            }
        }

        try {
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting propagate master change function ");

        //If nodes donot update their master info correctly, its their problem. So we aren't bothered about the
        // error code here. We return success
        return respCode[0];
    }

    void closeCommunicationsStubs() {
        System.out.println("Clearing Communication channels");
        for(Map.Entry<String, CommunicationStub> entry: communicationStubHashMap.entrySet()) {
            entry.getValue().closeCommunicationStub();;
        }
        System.out.println("Clearing Communication channels completed");
    }

}
