package service;

import com.grpc.protocol.keyvaluestoreGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CommunicationStub {
    String serverId;
    ManagedChannel channel;
    keyvaluestoreGrpc.keyvaluestoreStub communicationStub;
    boolean isAlive;

    CommunicationStub(String serverId, ServerProperties server) {
        this.serverId = serverId;
        this.channel = ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                .usePlaintext().build();
        this.communicationStub = keyvaluestoreGrpc.newStub(channel);
        this.isAlive = true;
    }

    CommunicationStub(ManagedChannel channel, keyvaluestoreGrpc.keyvaluestoreStub communicationStub, String serverId) {
        this.serverId = serverId;
        this.channel = channel;
        this.communicationStub = communicationStub;
        this.isAlive = true;
    }

    void closeCommunicationStub() {
        this.channel.shutdownNow();
    }
}
