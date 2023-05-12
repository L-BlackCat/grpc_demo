package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.example.GreeterGrpc;
import io.grpc.example.HelloWorldProto;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloWorldClient {
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

    public HelloWorldClient(String host, int port) {
        //  1.拿到通信channel
        channel = ManagedChannelBuilder.forAddress(host,port)
                .usePlaintext()
                .build();
        //  2.拿到通信对象
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(6, TimeUnit.SECONDS);
    }

    public void greet(String name){
        HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder().setName(name).build();
        HelloWorldProto.HelloReply response;
        try{
            //  3.请求
            response = blockingStub.sayHello(request);
        }catch (StatusRuntimeException e){
            logger.log(Level.WARNING,"RPC failed:{0}", e.getStatus());
            return;
        }
        // 4. 输出结果
        logger.info("Message from gRPC-Server" + response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        HelloWorldClient client = new HelloWorldClient("localhost", 50051);
        try{
            String user = "world";
            if(args.length > 0){
                user = args[0];
            }
            client.greet(user);
        }finally {
            //  5.关闭channel，释放资源
            client.shutdown();
        }
    }
}
