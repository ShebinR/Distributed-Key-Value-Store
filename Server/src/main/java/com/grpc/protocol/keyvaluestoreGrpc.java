package com.grpc.protocol;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: keyvaluestore.proto")
public final class keyvaluestoreGrpc {

  private keyvaluestoreGrpc() {}

  public static final String SERVICE_NAME = "keyvaluestore";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.GetRequest,
      com.grpc.protocol.Keyvaluestore.GetResponse> getGetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "get",
      requestType = com.grpc.protocol.Keyvaluestore.GetRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.GetResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.GetRequest,
      com.grpc.protocol.Keyvaluestore.GetResponse> getGetMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.GetRequest, com.grpc.protocol.Keyvaluestore.GetResponse> getGetMethod;
    if ((getGetMethod = keyvaluestoreGrpc.getGetMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getGetMethod = keyvaluestoreGrpc.getGetMethod) == null) {
          keyvaluestoreGrpc.getGetMethod = getGetMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.GetRequest, com.grpc.protocol.Keyvaluestore.GetResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "get"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.GetRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.GetResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("get"))
                  .build();
          }
        }
     }
     return getGetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.PutRequest,
      com.grpc.protocol.Keyvaluestore.PutResponse> getPutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "put",
      requestType = com.grpc.protocol.Keyvaluestore.PutRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.PutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.PutRequest,
      com.grpc.protocol.Keyvaluestore.PutResponse> getPutMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.PutRequest, com.grpc.protocol.Keyvaluestore.PutResponse> getPutMethod;
    if ((getPutMethod = keyvaluestoreGrpc.getPutMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getPutMethod = keyvaluestoreGrpc.getPutMethod) == null) {
          keyvaluestoreGrpc.getPutMethod = getPutMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.PutRequest, com.grpc.protocol.Keyvaluestore.PutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "put"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.PutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.PutResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("put"))
                  .build();
          }
        }
     }
     return getPutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.PropogatePutRequest,
      com.grpc.protocol.Keyvaluestore.PutResponse> getPropogatePutMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "propogatePut",
      requestType = com.grpc.protocol.Keyvaluestore.PropogatePutRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.PutResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.PropogatePutRequest,
      com.grpc.protocol.Keyvaluestore.PutResponse> getPropogatePutMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.PropogatePutRequest, com.grpc.protocol.Keyvaluestore.PutResponse> getPropogatePutMethod;
    if ((getPropogatePutMethod = keyvaluestoreGrpc.getPropogatePutMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getPropogatePutMethod = keyvaluestoreGrpc.getPropogatePutMethod) == null) {
          keyvaluestoreGrpc.getPropogatePutMethod = getPropogatePutMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.PropogatePutRequest, com.grpc.protocol.Keyvaluestore.PutResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "propogatePut"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.PropogatePutRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.PutResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("propogatePut"))
                  .build();
          }
        }
     }
     return getPropogatePutMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getUpdateFromMasterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateFromMaster",
      requestType = com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.DefaultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getUpdateFromMasterMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse> getUpdateFromMasterMethod;
    if ((getUpdateFromMasterMethod = keyvaluestoreGrpc.getUpdateFromMasterMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getUpdateFromMasterMethod = keyvaluestoreGrpc.getUpdateFromMasterMethod) == null) {
          keyvaluestoreGrpc.getUpdateFromMasterMethod = getUpdateFromMasterMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "updateFromMaster"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.DefaultResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("updateFromMaster"))
                  .build();
          }
        }
     }
     return getUpdateFromMasterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.RevertRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getRevertUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "revertUpdate",
      requestType = com.grpc.protocol.Keyvaluestore.RevertRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.DefaultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.RevertRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getRevertUpdateMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.RevertRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse> getRevertUpdateMethod;
    if ((getRevertUpdateMethod = keyvaluestoreGrpc.getRevertUpdateMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getRevertUpdateMethod = keyvaluestoreGrpc.getRevertUpdateMethod) == null) {
          keyvaluestoreGrpc.getRevertUpdateMethod = getRevertUpdateMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.RevertRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "revertUpdate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.RevertRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.DefaultResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("revertUpdate"))
                  .build();
          }
        }
     }
     return getRevertUpdateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.ChangeMasterRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getChangeMasterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "changeMaster",
      requestType = com.grpc.protocol.Keyvaluestore.ChangeMasterRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.DefaultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.ChangeMasterRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getChangeMasterMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.ChangeMasterRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse> getChangeMasterMethod;
    if ((getChangeMasterMethod = keyvaluestoreGrpc.getChangeMasterMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getChangeMasterMethod = keyvaluestoreGrpc.getChangeMasterMethod) == null) {
          keyvaluestoreGrpc.getChangeMasterMethod = getChangeMasterMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.ChangeMasterRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "changeMaster"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.ChangeMasterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.DefaultResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("changeMaster"))
                  .build();
          }
        }
     }
     return getChangeMasterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getAcceptNewMasterRoleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "acceptNewMasterRole",
      requestType = com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.DefaultResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest,
      com.grpc.protocol.Keyvaluestore.DefaultResponse> getAcceptNewMasterRoleMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse> getAcceptNewMasterRoleMethod;
    if ((getAcceptNewMasterRoleMethod = keyvaluestoreGrpc.getAcceptNewMasterRoleMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getAcceptNewMasterRoleMethod = keyvaluestoreGrpc.getAcceptNewMasterRoleMethod) == null) {
          keyvaluestoreGrpc.getAcceptNewMasterRoleMethod = getAcceptNewMasterRoleMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest, com.grpc.protocol.Keyvaluestore.DefaultResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "acceptNewMasterRole"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.DefaultResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("acceptNewMasterRole"))
                  .build();
          }
        }
     }
     return getAcceptNewMasterRoleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.EmptyRequest,
      com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse> getWhoIsMasterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "whoIsMaster",
      requestType = com.grpc.protocol.Keyvaluestore.EmptyRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.EmptyRequest,
      com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse> getWhoIsMasterMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.EmptyRequest, com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse> getWhoIsMasterMethod;
    if ((getWhoIsMasterMethod = keyvaluestoreGrpc.getWhoIsMasterMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getWhoIsMasterMethod = keyvaluestoreGrpc.getWhoIsMasterMethod) == null) {
          keyvaluestoreGrpc.getWhoIsMasterMethod = getWhoIsMasterMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.EmptyRequest, com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "whoIsMaster"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.EmptyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("whoIsMaster"))
                  .build();
          }
        }
     }
     return getWhoIsMasterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.EmptyRequest,
      com.grpc.protocol.Keyvaluestore.AreYouMasterResponse> getAreYouMasterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "areYouMaster",
      requestType = com.grpc.protocol.Keyvaluestore.EmptyRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.AreYouMasterResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.EmptyRequest,
      com.grpc.protocol.Keyvaluestore.AreYouMasterResponse> getAreYouMasterMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.EmptyRequest, com.grpc.protocol.Keyvaluestore.AreYouMasterResponse> getAreYouMasterMethod;
    if ((getAreYouMasterMethod = keyvaluestoreGrpc.getAreYouMasterMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getAreYouMasterMethod = keyvaluestoreGrpc.getAreYouMasterMethod) == null) {
          keyvaluestoreGrpc.getAreYouMasterMethod = getAreYouMasterMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.EmptyRequest, com.grpc.protocol.Keyvaluestore.AreYouMasterResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "areYouMaster"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.EmptyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.AreYouMasterResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("areYouMaster"))
                  .build();
          }
        }
     }
     return getAreYouMasterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest,
      com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse> getGetUpdatesAfterTSMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getUpdatesAfterTS",
      requestType = com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest.class,
      responseType = com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest,
      com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse> getGetUpdatesAfterTSMethod() {
    io.grpc.MethodDescriptor<com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest, com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse> getGetUpdatesAfterTSMethod;
    if ((getGetUpdatesAfterTSMethod = keyvaluestoreGrpc.getGetUpdatesAfterTSMethod) == null) {
      synchronized (keyvaluestoreGrpc.class) {
        if ((getGetUpdatesAfterTSMethod = keyvaluestoreGrpc.getGetUpdatesAfterTSMethod) == null) {
          keyvaluestoreGrpc.getGetUpdatesAfterTSMethod = getGetUpdatesAfterTSMethod = 
              io.grpc.MethodDescriptor.<com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest, com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "keyvaluestore", "getUpdatesAfterTS"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new keyvaluestoreMethodDescriptorSupplier("getUpdatesAfterTS"))
                  .build();
          }
        }
     }
     return getGetUpdatesAfterTSMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static keyvaluestoreStub newStub(io.grpc.Channel channel) {
    return new keyvaluestoreStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static keyvaluestoreBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new keyvaluestoreBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static keyvaluestoreFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new keyvaluestoreFutureStub(channel);
  }

  /**
   */
  public static abstract class keyvaluestoreImplBase implements io.grpc.BindableService {

    /**
     */
    public void get(com.grpc.protocol.Keyvaluestore.GetRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.GetResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMethod(), responseObserver);
    }

    /**
     */
    public void put(com.grpc.protocol.Keyvaluestore.PutRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.PutResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPutMethod(), responseObserver);
    }

    /**
     */
    public void propogatePut(com.grpc.protocol.Keyvaluestore.PropogatePutRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.PutResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPropogatePutMethod(), responseObserver);
    }

    /**
     */
    public void updateFromMaster(com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateFromMasterMethod(), responseObserver);
    }

    /**
     */
    public void revertUpdate(com.grpc.protocol.Keyvaluestore.RevertRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRevertUpdateMethod(), responseObserver);
    }

    /**
     */
    public void changeMaster(com.grpc.protocol.Keyvaluestore.ChangeMasterRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getChangeMasterMethod(), responseObserver);
    }

    /**
     */
    public void acceptNewMasterRole(com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAcceptNewMasterRoleMethod(), responseObserver);
    }

    /**
     */
    public void whoIsMaster(com.grpc.protocol.Keyvaluestore.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getWhoIsMasterMethod(), responseObserver);
    }

    /**
     */
    public void areYouMaster(com.grpc.protocol.Keyvaluestore.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.AreYouMasterResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAreYouMasterMethod(), responseObserver);
    }

    /**
     */
    public void getUpdatesAfterTS(com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetUpdatesAfterTSMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.GetRequest,
                com.grpc.protocol.Keyvaluestore.GetResponse>(
                  this, METHODID_GET)))
          .addMethod(
            getPutMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.PutRequest,
                com.grpc.protocol.Keyvaluestore.PutResponse>(
                  this, METHODID_PUT)))
          .addMethod(
            getPropogatePutMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.PropogatePutRequest,
                com.grpc.protocol.Keyvaluestore.PutResponse>(
                  this, METHODID_PROPOGATE_PUT)))
          .addMethod(
            getUpdateFromMasterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest,
                com.grpc.protocol.Keyvaluestore.DefaultResponse>(
                  this, METHODID_UPDATE_FROM_MASTER)))
          .addMethod(
            getRevertUpdateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.RevertRequest,
                com.grpc.protocol.Keyvaluestore.DefaultResponse>(
                  this, METHODID_REVERT_UPDATE)))
          .addMethod(
            getChangeMasterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.ChangeMasterRequest,
                com.grpc.protocol.Keyvaluestore.DefaultResponse>(
                  this, METHODID_CHANGE_MASTER)))
          .addMethod(
            getAcceptNewMasterRoleMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest,
                com.grpc.protocol.Keyvaluestore.DefaultResponse>(
                  this, METHODID_ACCEPT_NEW_MASTER_ROLE)))
          .addMethod(
            getWhoIsMasterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.EmptyRequest,
                com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse>(
                  this, METHODID_WHO_IS_MASTER)))
          .addMethod(
            getAreYouMasterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.EmptyRequest,
                com.grpc.protocol.Keyvaluestore.AreYouMasterResponse>(
                  this, METHODID_ARE_YOU_MASTER)))
          .addMethod(
            getGetUpdatesAfterTSMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest,
                com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse>(
                  this, METHODID_GET_UPDATES_AFTER_TS)))
          .build();
    }
  }

  /**
   */
  public static final class keyvaluestoreStub extends io.grpc.stub.AbstractStub<keyvaluestoreStub> {
    private keyvaluestoreStub(io.grpc.Channel channel) {
      super(channel);
    }

    private keyvaluestoreStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected keyvaluestoreStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new keyvaluestoreStub(channel, callOptions);
    }

    /**
     */
    public void get(com.grpc.protocol.Keyvaluestore.GetRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.GetResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void put(com.grpc.protocol.Keyvaluestore.PutRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.PutResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void propogatePut(com.grpc.protocol.Keyvaluestore.PropogatePutRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.PutResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPropogatePutMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateFromMaster(com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateFromMasterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void revertUpdate(com.grpc.protocol.Keyvaluestore.RevertRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRevertUpdateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void changeMaster(com.grpc.protocol.Keyvaluestore.ChangeMasterRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getChangeMasterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void acceptNewMasterRole(com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAcceptNewMasterRoleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void whoIsMaster(com.grpc.protocol.Keyvaluestore.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWhoIsMasterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void areYouMaster(com.grpc.protocol.Keyvaluestore.EmptyRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.AreYouMasterResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAreYouMasterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUpdatesAfterTS(com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest request,
        io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetUpdatesAfterTSMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class keyvaluestoreBlockingStub extends io.grpc.stub.AbstractStub<keyvaluestoreBlockingStub> {
    private keyvaluestoreBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private keyvaluestoreBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected keyvaluestoreBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new keyvaluestoreBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.GetResponse get(com.grpc.protocol.Keyvaluestore.GetRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.PutResponse put(com.grpc.protocol.Keyvaluestore.PutRequest request) {
      return blockingUnaryCall(
          getChannel(), getPutMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.PutResponse propogatePut(com.grpc.protocol.Keyvaluestore.PropogatePutRequest request) {
      return blockingUnaryCall(
          getChannel(), getPropogatePutMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.DefaultResponse updateFromMaster(com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateFromMasterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.DefaultResponse revertUpdate(com.grpc.protocol.Keyvaluestore.RevertRequest request) {
      return blockingUnaryCall(
          getChannel(), getRevertUpdateMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.DefaultResponse changeMaster(com.grpc.protocol.Keyvaluestore.ChangeMasterRequest request) {
      return blockingUnaryCall(
          getChannel(), getChangeMasterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.DefaultResponse acceptNewMasterRole(com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest request) {
      return blockingUnaryCall(
          getChannel(), getAcceptNewMasterRoleMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse whoIsMaster(com.grpc.protocol.Keyvaluestore.EmptyRequest request) {
      return blockingUnaryCall(
          getChannel(), getWhoIsMasterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.AreYouMasterResponse areYouMaster(com.grpc.protocol.Keyvaluestore.EmptyRequest request) {
      return blockingUnaryCall(
          getChannel(), getAreYouMasterMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse getUpdatesAfterTS(com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetUpdatesAfterTSMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class keyvaluestoreFutureStub extends io.grpc.stub.AbstractStub<keyvaluestoreFutureStub> {
    private keyvaluestoreFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private keyvaluestoreFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected keyvaluestoreFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new keyvaluestoreFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.GetResponse> get(
        com.grpc.protocol.Keyvaluestore.GetRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.PutResponse> put(
        com.grpc.protocol.Keyvaluestore.PutRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPutMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.PutResponse> propogatePut(
        com.grpc.protocol.Keyvaluestore.PropogatePutRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPropogatePutMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.DefaultResponse> updateFromMaster(
        com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateFromMasterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.DefaultResponse> revertUpdate(
        com.grpc.protocol.Keyvaluestore.RevertRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRevertUpdateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.DefaultResponse> changeMaster(
        com.grpc.protocol.Keyvaluestore.ChangeMasterRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getChangeMasterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.DefaultResponse> acceptNewMasterRole(
        com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAcceptNewMasterRoleMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse> whoIsMaster(
        com.grpc.protocol.Keyvaluestore.EmptyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getWhoIsMasterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.AreYouMasterResponse> areYouMaster(
        com.grpc.protocol.Keyvaluestore.EmptyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAreYouMasterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse> getUpdatesAfterTS(
        com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetUpdatesAfterTSMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET = 0;
  private static final int METHODID_PUT = 1;
  private static final int METHODID_PROPOGATE_PUT = 2;
  private static final int METHODID_UPDATE_FROM_MASTER = 3;
  private static final int METHODID_REVERT_UPDATE = 4;
  private static final int METHODID_CHANGE_MASTER = 5;
  private static final int METHODID_ACCEPT_NEW_MASTER_ROLE = 6;
  private static final int METHODID_WHO_IS_MASTER = 7;
  private static final int METHODID_ARE_YOU_MASTER = 8;
  private static final int METHODID_GET_UPDATES_AFTER_TS = 9;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final keyvaluestoreImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(keyvaluestoreImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET:
          serviceImpl.get((com.grpc.protocol.Keyvaluestore.GetRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.GetResponse>) responseObserver);
          break;
        case METHODID_PUT:
          serviceImpl.put((com.grpc.protocol.Keyvaluestore.PutRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.PutResponse>) responseObserver);
          break;
        case METHODID_PROPOGATE_PUT:
          serviceImpl.propogatePut((com.grpc.protocol.Keyvaluestore.PropogatePutRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.PutResponse>) responseObserver);
          break;
        case METHODID_UPDATE_FROM_MASTER:
          serviceImpl.updateFromMaster((com.grpc.protocol.Keyvaluestore.UpdateFromMasterRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse>) responseObserver);
          break;
        case METHODID_REVERT_UPDATE:
          serviceImpl.revertUpdate((com.grpc.protocol.Keyvaluestore.RevertRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse>) responseObserver);
          break;
        case METHODID_CHANGE_MASTER:
          serviceImpl.changeMaster((com.grpc.protocol.Keyvaluestore.ChangeMasterRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse>) responseObserver);
          break;
        case METHODID_ACCEPT_NEW_MASTER_ROLE:
          serviceImpl.acceptNewMasterRole((com.grpc.protocol.Keyvaluestore.AcceptNewMasterRoleRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.DefaultResponse>) responseObserver);
          break;
        case METHODID_WHO_IS_MASTER:
          serviceImpl.whoIsMaster((com.grpc.protocol.Keyvaluestore.EmptyRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.WhoIsMasterResponse>) responseObserver);
          break;
        case METHODID_ARE_YOU_MASTER:
          serviceImpl.areYouMaster((com.grpc.protocol.Keyvaluestore.EmptyRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.AreYouMasterResponse>) responseObserver);
          break;
        case METHODID_GET_UPDATES_AFTER_TS:
          serviceImpl.getUpdatesAfterTS((com.grpc.protocol.Keyvaluestore.FindUpdatesAfterTSRequest) request,
              (io.grpc.stub.StreamObserver<com.grpc.protocol.Keyvaluestore.UpdatesAfterTSResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class keyvaluestoreBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    keyvaluestoreBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.grpc.protocol.Keyvaluestore.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("keyvaluestore");
    }
  }

  private static final class keyvaluestoreFileDescriptorSupplier
      extends keyvaluestoreBaseDescriptorSupplier {
    keyvaluestoreFileDescriptorSupplier() {}
  }

  private static final class keyvaluestoreMethodDescriptorSupplier
      extends keyvaluestoreBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    keyvaluestoreMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (keyvaluestoreGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new keyvaluestoreFileDescriptorSupplier())
              .addMethod(getGetMethod())
              .addMethod(getPutMethod())
              .addMethod(getPropogatePutMethod())
              .addMethod(getUpdateFromMasterMethod())
              .addMethod(getRevertUpdateMethod())
              .addMethod(getChangeMasterMethod())
              .addMethod(getAcceptNewMasterRoleMethod())
              .addMethod(getWhoIsMasterMethod())
              .addMethod(getAreYouMasterMethod())
              .addMethod(getGetUpdatesAfterTSMethod())
              .build();
        }
      }
    }
    return result;
  }
}
