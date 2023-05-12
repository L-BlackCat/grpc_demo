package org.example;

import io.grpc.example.GreeterGrpc;
import io.grpc.example.HelloWorldProto;
import io.grpc.stub.StreamObserver;

/**
 * 实现一个实现服务接口的类
 * GreeterGrpc.GreeterImplBase 这个就是接口.
 * GreeterImpl 我们需要继承他的,实现方法回调
 */
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloWorldProto.HelloRequest request, StreamObserver<HelloWorldProto.HelloReply> responseObserver) {
        //  请求结果，自己定义
        HelloWorldProto.HelloReply reply = HelloWorldProto.HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        //  这种写法是observer，异步写法
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
        System.out.println("Message from gRPC-Client:" + request.getName());
        System.out.println("Message Response:" + reply.getMessage());
    }
}
