package org.hyperledger.fabric.protos.peer;

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
 * <pre>
 * Interface exported by the events server
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.11.0)",
    comments = "Source: peer/events.proto")
public final class EventsGrpc {

  private EventsGrpc() {}

  public static final String SERVICE_NAME = "protos.Events";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getChatMethod()} instead. 
  public static final io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent,
      org.hyperledger.fabric.protos.peer.PeerEvents.Event> METHOD_CHAT = getChatMethodHelper();

  private static volatile io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent,
      org.hyperledger.fabric.protos.peer.PeerEvents.Event> getChatMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent,
      org.hyperledger.fabric.protos.peer.PeerEvents.Event> getChatMethod() {
    return getChatMethodHelper();
  }

  private static io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent,
      org.hyperledger.fabric.protos.peer.PeerEvents.Event> getChatMethodHelper() {
    io.grpc.MethodDescriptor<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent, org.hyperledger.fabric.protos.peer.PeerEvents.Event> getChatMethod;
    if ((getChatMethod = EventsGrpc.getChatMethod) == null) {
      synchronized (EventsGrpc.class) {
        if ((getChatMethod = EventsGrpc.getChatMethod) == null) {
          EventsGrpc.getChatMethod = getChatMethod = 
              io.grpc.MethodDescriptor.<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent, org.hyperledger.fabric.protos.peer.PeerEvents.Event>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "protos.Events", "Chat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.hyperledger.fabric.protos.peer.PeerEvents.Event.getDefaultInstance()))
                  .setSchemaDescriptor(new EventsMethodDescriptorSupplier("Chat"))
                  .build();
          }
        }
     }
     return getChatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EventsStub newStub(io.grpc.Channel channel) {
    return new EventsStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EventsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new EventsBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EventsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new EventsFutureStub(channel);
  }

  /**
   * <pre>
   * Interface exported by the events server
   * </pre>
   */
  public static abstract class EventsImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * event chatting using Event
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent> chat(
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.PeerEvents.Event> responseObserver) {
      return asyncUnimplementedStreamingCall(getChatMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getChatMethodHelper(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent,
                org.hyperledger.fabric.protos.peer.PeerEvents.Event>(
                  this, METHODID_CHAT)))
          .build();
    }
  }

  /**
   * <pre>
   * Interface exported by the events server
   * </pre>
   */
  public static final class EventsStub extends io.grpc.stub.AbstractStub<EventsStub> {
    private EventsStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EventsStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventsStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EventsStub(channel, callOptions);
    }

    /**
     * <pre>
     * event chatting using Event
     * </pre>
     */
    public io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.PeerEvents.SignedEvent> chat(
        io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.PeerEvents.Event> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getChatMethodHelper(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * Interface exported by the events server
   * </pre>
   */
  public static final class EventsBlockingStub extends io.grpc.stub.AbstractStub<EventsBlockingStub> {
    private EventsBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EventsBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventsBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EventsBlockingStub(channel, callOptions);
    }
  }

  /**
   * <pre>
   * Interface exported by the events server
   * </pre>
   */
  public static final class EventsFutureStub extends io.grpc.stub.AbstractStub<EventsFutureStub> {
    private EventsFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EventsFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventsFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EventsFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CHAT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EventsImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(EventsImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.chat(
              (io.grpc.stub.StreamObserver<org.hyperledger.fabric.protos.peer.PeerEvents.Event>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class EventsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EventsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.hyperledger.fabric.protos.peer.PeerEvents.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Events");
    }
  }

  private static final class EventsFileDescriptorSupplier
      extends EventsBaseDescriptorSupplier {
    EventsFileDescriptorSupplier() {}
  }

  private static final class EventsMethodDescriptorSupplier
      extends EventsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    EventsMethodDescriptorSupplier(String methodName) {
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
      synchronized (EventsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EventsFileDescriptorSupplier())
              .addMethod(getChatMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
