package service;

import com.grpc.protocol.Keyvaluestore;
import com.grpc.protocol.keyvaluestoreGrpc;
import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import io.grpc.stub.StreamObserver;
import utils.Constants;
import utils.DBConnection;
import utils.MethodUtils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class KeyValueStoreServerImpl extends keyvaluestoreGrpc.keyvaluestoreImplBase {

    private HashMap<String, String> hashtable;
    private DBConnection db;

    //Object used to establish communication channels and send messages to other servers
    private Communication comm;

    public KeyValueStoreServerImpl(ServerProperties obj, List<ServerProperties> serverList, int masterIndex){
        //Form DB connection url based on the server number.
        db = new DBConnection(Constants.JDBC_CONN_PREFIX + Constants.DB_NAME_PREFIX
                + String.valueOf(obj.getServerId())
                + Constants.DB_EXTENSION);
        // Need DB Connection
        comm = new Communication(serverList, obj.getIsMaster(), obj, db);

        //Fetch values from db
        System.out.println();
        System.out.println("Updating in-memory Key Value Store");
        if(db.conn != null){
            hashtable = db.selectAll();
        }else{
            System.out.println("Connection is null");
        }
        if(hashtable == null){
            hashtable = new HashMap<>();
        }
        System.out.println();
    }

    /**
     * Implementation of the Get method of the service in proto file
     * @param request Input request object
     * @param responseObserver Stream response object
     */
    @Override
    public void get(Keyvaluestore.GetRequest request, StreamObserver<Keyvaluestore.GetResponse> responseObserver) {
        System.out.println();
        System.out.println("Get Request from Client for Key : " + request.getKey());
        //Create a response builder
        Keyvaluestore.GetResponse.Builder responseBuilder = Keyvaluestore.GetResponse.newBuilder();

//        System.out.println("Get Request received for " + request.getKey());
        //Fetch the value from hash table
        String key = request.getKey();
        // Default for value is empty
        String value = StringUtil.EMPTY_STRING;
        // responseCode is 0 on success, 1 on key not found and -1 on error

        int responseCode;
        if(key == null || hashtable == null){
            responseCode = Constants.OPERATION_FAILURE_STATUS_CODE;
        } else {
            if (!hashtable.containsKey(key)) {
                responseCode = 1;
            } else {
                value = hashtable.get(key);
                responseCode = Constants.OPERATION_SUCCESS_STATUS_CODE;
            }
        }
//        do{
//            if(key == null || hashtable == null){
//                responseCode = Constants.OPERATION_FAILURE_STATUS_CODE;
//                break;
//            }
//            if(!hashtable.containsKey(key)){
//                responseCode = 1;
//                break;
//            }
//            value = hashtable.get(key);
//            responseCode = Constants.OPERATION_SUCCESS_STATUS_CODE;
//        }while(false);

        responseBuilder.setValue(value).setResponseCode(responseCode);

        System.out.println("Sending Get response with value " + value + " and error code " + responseCode);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
        System.out.println();
    }

    /**
     * Implementation of the Put method of the service in proto file
     * @param request Input request object
     * @param responseObserver Stream response object
     */
    @Override
    public void put(Keyvaluestore.PutRequest request, StreamObserver<Keyvaluestore.PutResponse> responseObserver) {

        String key = request.getKey();
        String value = request.getValue();

        System.out.println();
        System.out.println("Put request from Client : Key " + request.getKey()  + " Value : " + request.getValue());
        Keyvaluestore.PutResponse.Builder responseBuilder = handlePut(key, value);
        System.out.println("Finished serving put request ");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
        System.out.println();
    }

    /**
     * Handle put request from non-master servers
     * @param request Input request object
     * @param responseObserver Stream response object
     */
    @Override
    public void propogatePut(Keyvaluestore.PropogatePutRequest request, StreamObserver<Keyvaluestore.PutResponse> responseObserver) {
        System.out.println("Put request from data-node");
        Keyvaluestore.PutResponse.Builder responseBuilder;
        do{
            responseBuilder = Keyvaluestore.PutResponse.newBuilder();

            //if I am not the master, then I don't serve these requests
            if(!comm.nodeProperty.getIsMaster()){
                responseBuilder.setOldValue("").setResponseCode(Constants.OPERATION_FAILURE_STATUS_CODE);
                break;
            }
            String key = request.getKey();
            String value = request.getValue();
            responseBuilder = handlePut(key, value);
            System.out.println("Finished serving put request ");

        }while(false);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    private Keyvaluestore.PutResponse.Builder handlePut(String key, String value){
        Keyvaluestore.PutResponse.Builder responseBuilder = Keyvaluestore.PutResponse.newBuilder();

        //Default old value is empty
        String oldValue = StringUtil.EMPTY_STRING;
        //response code is 0 on key already exists success, 1 on new key-value added success, -1 on failure
        int responseCode;
        boolean isNew;

        do{
            if(key == null || value == null || hashtable == null){
                responseCode = Constants.OPERATION_FAILURE_STATUS_CODE;
                break;
            }

            if(hashtable.containsKey(key)){
                responseCode = Constants.OPERATION_SUCCESS_STATUS_CODE;
                oldValue = hashtable.get(key);
                isNew = false;
            }else{
                responseCode = 1;
                isNew = true;
            }
            //if the operation is failure, then mark the put as failed. else it is either 0 or 1 based on if
            // its existing key or new ones.
            if(updateData(key,value, oldValue, isNew) == Constants.OPERATION_FAILURE_STATUS_CODE){
                responseCode = Constants.OPERATION_FAILURE_STATUS_CODE;
            }

        }while(false);
        System.out.println("Handle put : " + responseCode);
        responseBuilder.setOldValue(oldValue).setResponseCode(responseCode);
        return responseBuilder;
    }

    /**
     * Api to update the server's db and hashtable for values entered using other servers.
     * Master sends this update
     * @param request
     * @param responseObserver
     */
    @Override
    public void updateFromMaster(Keyvaluestore.UpdateFromMasterRequest request, StreamObserver<Keyvaluestore.DefaultResponse> responseObserver) {
        System.out.println();
        System.out.println("Update request from master-node");
        Keyvaluestore.DefaultResponse.Builder responseBuilder = Keyvaluestore.DefaultResponse.newBuilder();

        String key = request.getKey();
        String value = request.getValue();
        long timeStamp = request.getTimeStamp();

        //System.out.println("Server "+ properties.getServerId() + " contains key " + hashtable.containsKey(key));
        int responseCode = updateInternalData(key, value, !hashtable.containsKey(key), timeStamp);

        responseBuilder.setResponseCode(responseCode);

        System.out.println("Finished serving update request from master-node");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
        System.out.println();
    }

    /**
     * Api to revert the unsuccessful changes to maintain consistency. This request is sent by the master
     * @param request
     * @param responseObserver
     */
    @Override
    public void revertUpdate(Keyvaluestore.RevertRequest request, StreamObserver<Keyvaluestore.DefaultResponse> responseObserver) {
        System.out.println("Revert request from master");
        Keyvaluestore.DefaultResponse.Builder responseBuilder = Keyvaluestore.DefaultResponse.newBuilder();

        String key = request.getKey();
        String value = request.getOldValue();
        boolean isNew = request.getIsNew();

        int responseCode = revertInternalData(key, value, isNew);

        responseBuilder.setResponseCode(responseCode);

        System.out.println("Finished serving revert request ");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void changeMaster(Keyvaluestore.ChangeMasterRequest request, StreamObserver<Keyvaluestore.DefaultResponse> responseObserver) {
        System.out.println("Change master request from new master");
        Keyvaluestore.DefaultResponse.Builder responseBuilder = Keyvaluestore.DefaultResponse.newBuilder();
        int responseCode;
        do{

            String host = request.getHostname();
            int port = request.getPort();
            int index = MethodUtils.findServerIndexFromAddress(comm.serverList, host, port);

            if(index == Constants.OPERATION_FAILURE_STATUS_CODE){
                System.out.println("master config not found in my serverlist.");
                responseCode = Constants.OPERATION_FAILURE_STATUS_CODE;
                break;
            }
            responseCode = updateInternalMasterInfo(index, false);

        }while(false);

        responseBuilder.setResponseCode(responseCode);

        System.out.println("Finished serving change master request ");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void acceptNewMasterRole(Keyvaluestore.AcceptNewMasterRoleRequest request, StreamObserver<Keyvaluestore.DefaultResponse> responseObserver) {
        System.out.println("Accept new master request from another server");

        Keyvaluestore.DefaultResponse.Builder responseBuilder = Keyvaluestore.DefaultResponse.newBuilder();
        int responseCode;

        if(comm.nodeProperty.getIsMaster()) {
            // Already got the election result from some other server. So ignore
            responseCode = Constants.OPERATION_SUCCESS_STATUS_CODE;
        }else{
            int index = MethodUtils.findServerIndexFromServerId(comm.serverList, comm.nodeProperty.getServerId());

            if(index == Constants.OPERATION_FAILURE_STATUS_CODE){
                System.out.println("Can't find my own config...");
                responseCode = Constants.OPERATION_FAILURE_STATUS_CODE;
            }else{
                responseCode = updateInternalMasterInfo(index, true);
            }
        }
        responseBuilder.setResponseCode(responseCode);

        System.out.println("Finished serving accept new master role request ");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    /**
     * Method to update the data to other servers and/or internally depending on who the master is
     * @param key - The key to update in db
     * @param value -  the value for the key
     * @param isNew - Is the key-value pair being added newly to the db
     * @return - error code
     */
    private int updateData(String key, String value, String oldValue, boolean isNew){
        int updateResult = Constants.OPERATION_SUCCESS_STATUS_CODE;
        do{
            //If this server instance is a master, then update db and propagate to other servers
            //else propagate to the master server and DONOT update the db
            if(comm.nodeProperty.getIsMaster()){
                long timeStamp = System.currentTimeMillis();
                updateResult = updateInternalData(key, value, isNew, timeStamp);
                //return on internal update failure
                if(updateResult == Constants.OPERATION_FAILURE_STATUS_CODE){
                    break;
                }
                //If propogate fails, then db will be inconsistent, then the operation is a failure
                //Rollback the transaction and propogate the rollback message
                if(comm.propagateUpdate(key, value, timeStamp) == Constants.OPERATION_FAILURE_STATUS_CODE){
                    updateResult = Constants.OPERATION_FAILURE_STATUS_CODE;
                    if(revertUpdate(key, oldValue, isNew) == Constants.OPERATION_FAILURE_STATUS_CODE){
                        System.out.println("Revert failed..");
                    }
                    break;
                }
            }else{
                updateResult = comm.sendUpdateToMaster(key, value, comm.masterIndex);

                /* Master Failure */
                if(updateResult == Constants.SERVER_UNAVAILABLE_STATUS_CODE){
                    //Master failed. So the next master candidate is found and an update is sent to it
                    int masterCandidate;
                    int count = 0;
                    int informMasterResponse;
                    do{
                        masterCandidate = findNewMasterCandidate(comm.masterIndex + count);
                        count++;
                        System.out.println("New master candidate index " + masterCandidate);
                        //If I am the master, do the necessary steps here
                        if(masterCandidate == MethodUtils.findServerIndexFromServerId(comm.serverList, comm.nodeProperty.getServerId())){
                            informMasterResponse = updateInternalMasterInfo(masterCandidate, true);
                        }else{
                            informMasterResponse = comm.informNewMaster(masterCandidate);
                        }

                    } while((informMasterResponse == Constants.OPERATION_FAILURE_STATUS_CODE
                            || informMasterResponse == Constants.SERVER_UNAVAILABLE_STATUS_CODE)
                            && count < comm.serverList.size());
                    //mark this request as a failure because the new master change hasn't propogated yet
                    updateResult = -1;
                    break;
                }
            }
        }while(false);

        System.out.println("Master: " + comm.nodeProperty.getIsMaster() +
                "\tExiting update function with resp code " + updateResult);

        return updateResult;
    }



    /**
     * Method to find the index of next master candidate
     * @param - current master index to consider
     * @return - index of new master candidate
     */
    private int findNewMasterCandidate(int currMasterIndex){
        //Return the next index of the current master
        return (currMasterIndex + 1) % comm.serverList.size();
    }


    /**
     *
     * @param newMasterIndex
     * @param isMaster
     * @return
     */
    private int updateInternalMasterInfo(int newMasterIndex, boolean isMaster){
        //Set the isMaster of old master to false
        comm.serverList.get(comm.masterIndex).setIsMaster(false);

        comm.masterIndex = newMasterIndex;
        comm.serverList.get(comm.masterIndex).setIsMaster(true);

        //Create the required communication channels
        comm.createRequiredCommunicationChannels(isMaster);

        if(isMaster){
            //change the properties instance field as well
            comm.nodeProperty.setIsMaster(true);
            comm.propagateMasterChange(comm.nodeProperty.getHost(), comm.nodeProperty.getPort());
        }

        return Constants.OPERATION_SUCCESS_STATUS_CODE;
    }

    /**
     * Called from the master if update communication to any of the servers fails
     * Updates the internal values and also propogates to other servers
     * @param key - The key to revert
     * @param oldValue - the value to revert to
     * @param isNew - Indicates if the operation to revert was an insert or update
     * @return - responseCode
     */
    private int revertUpdate(String key, String oldValue, boolean isNew){
        int respCode;
        respCode = revertInternalData(key, oldValue, isNew);
        if(respCode != Constants.OPERATION_FAILURE_STATUS_CODE){
            respCode = comm.propagateRevert(key, oldValue, isNew);
        }
        return respCode;
    }

    /**
     * This method is used to update the internal data structure and db with the new values
     * @param key - The key to update in db
     * @param value -  the value for the key
     * @param isNew - Is the key-value pair being added newly to the db
     * @return - return 0 on success and -1 on failure
     */
    private int updateInternalData(String key, String value, boolean isNew, long timeStamp){
        System.out.println("Update internally....");
        //update in-memory hash table
        synchronized (hashtable) {
            if(hashtable.containsKey(key) && hashtable.get(key).equals(value))
                return Constants.OPERATION_SUCCESS_STATUS_CODE;
            if(hashtable.containsKey(key))
                isNew = false;
            hashtable.put(key, value);
        }
        if(updateDb(key, value, isNew, false, timeStamp) == Constants.OPERATION_FAILURE_STATUS_CODE){
            hashtable.remove(key);
            return Constants.OPERATION_FAILURE_STATUS_CODE;
        }

        return Constants.OPERATION_SUCCESS_STATUS_CODE;
    }

    /**
     * This method is used to revert the internal data structure and db to reflect a consistent state
     * @param key - The key to update in db
     * @param oldValue -  the value to revert to
     * @param isNew - Is the key-value pair was added newly to the db
     * @return - return 0 on success and -1 on failure
     */
    private int revertInternalData(String key, String oldValue, boolean isNew){
        System.out.println("Revert internally....");

        if(updateDb(key, oldValue, isNew, true, 0) == Constants.OPERATION_FAILURE_STATUS_CODE){
            return Constants.OPERATION_FAILURE_STATUS_CODE;
        }
        //update in-memory hash table after the db
        if(isNew){
            hashtable.remove(key);
        }else{
            hashtable.put(key, oldValue);
        }
        return Constants.OPERATION_SUCCESS_STATUS_CODE;
    }

    /**
     * This method is used to update the db to either add new key-value pair to update the existing ones
     * @param key - The key to update in db
     * @param value -  the value for the key
     * @param isNew - Is the key-value pair being added newly to the db
     * @param isRevert - Is the update db a revert call? If so delete the given key
     * @return - return 0 on success and -1 on failure
     */
    private int updateDb(String key, String value, boolean isNew, boolean isRevert, long timeStamp){
        if(db == null || db.conn == null){
            return Constants.OPERATION_FAILURE_STATUS_CODE;
        }
        if(isNew){
            return isRevert ? db.delete(key) : db.insert(key, value, timeStamp);
        }else{
            return db.update(key, value, timeStamp);
        }
    }

    public void whoIsMaster(Keyvaluestore.EmptyRequest request,
                            StreamObserver<Keyvaluestore.WhoIsMasterResponse> responseStreamObserver) {
        System.out.println();
        System.out.println("Received : WhoIsMaster message");
        Keyvaluestore.WhoIsMasterResponse.Builder response = Keyvaluestore.WhoIsMasterResponse.newBuilder();
        int masterIndex = MethodUtils.findMaster(comm.serverList);
        ServerProperties masterNode = comm.serverList.get(masterIndex);
        response.setPort(masterNode.getPort());
        response.setServerId(masterNode.getServerId());
        response.setHostName(masterNode.getHost());
        responseStreamObserver.onNext(response.build());
        responseStreamObserver.onCompleted();
        System.out.println("Replied : " + response.getServerId());
        System.out.println();
    }

    public void areYouMaster(Keyvaluestore.EmptyRequest request,
                             StreamObserver<Keyvaluestore.AreYouMasterResponse> responseStreamObserver) {
        System.out.println();
        System.out.println("Received : AreYouMaster message");
        Keyvaluestore.AreYouMasterResponse.Builder response = Keyvaluestore.AreYouMasterResponse.newBuilder();
        if(this.comm.nodeProperty.getIsMaster()) {
            response.setResponse(true).build();
        } else {
            response.setResponse(false).build();
        }
        responseStreamObserver.onNext(response.build());
        responseStreamObserver.onCompleted();
        System.out.println("Replied : " + response.getResponse());
        System.out.println();
    }

    public void getUpdatesAfterTS(Keyvaluestore.FindUpdatesAfterTSRequest request,
                                StreamObserver<Keyvaluestore.UpdatesAfterTSResponse> response) {
        System.out.println();
        System.out.println("Received : getUpdatesAfter message for " + request.getLastKnownTimeStamp()
                + " from " + request.getServerId());
        List<String> records = this.comm.dbConnection.getRecordsAfterSequenceNumber
                (request.getLastKnownTimeStamp());

        if(!comm.communicationStubHashMap.containsKey(request.getServerId())) {
            // TO DO
//            comm.communicationStubHashMap.put(request.getServerId(), new Communication(
//                    request.getServerId(), new ServerProperties();
//            ) );
            System.out.println("WARN: Master Node: Addition of new server not in config!");
        }
        comm.communicationStubHashMap.get(request.getServerId()).isAlive = true;

        // Pack all records as new line separated string
        StringBuilder r = new StringBuilder();
        boolean noUpdate = false;
        if(records.size() != 0) {
            for (int i = 0; i < records.size(); i++) {
                r.append(records.get(i));
                r.append("\n");
            }
        } else {
            noUpdate = true;
        }

        Keyvaluestore.UpdatesAfterTSResponse.Builder reply = Keyvaluestore.UpdatesAfterTSResponse.newBuilder();
        reply.setRecords(r.toString());
        reply.setNoUpdates(noUpdate);
        response.onNext(reply.build());
        response.onCompleted();

        System.out.println("Replied : With set of record (size : " + records.size() + ")");
        System.out.println();
    }

    public void closeAllCommunication() {
        comm.closeCommunicationsStubs();
        db.closeConnection();
    }
}

//keyvaluestoreGrpc.keyvaluestoreBlockingStub propagateStub = keyvaluestoreGrpc.newBlockingStub(channel);

//Keyvaluestore.PutResponse response = propagateStub.put(updateRequest);
