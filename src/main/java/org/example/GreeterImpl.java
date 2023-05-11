package org.example;

import io.grpc.example.GreeterGrpc;
import io.grpc.example.HelloWorldProto;
import io.grpc.stub.StreamObserver;

/**
 * 实现一个实现服务接口的类
 */
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloWorldProto.HelloRequest request, StreamObserver<HelloWorldProto.HelloReply> responseObserver) {
        HelloWorldProto.HelloReply reply = HelloWorldProto.HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
        System.out.println("Message from gRPC-Client:" + request.getName());
        System.out.println("Message Response:" + reply.getMessage());
    }
}
