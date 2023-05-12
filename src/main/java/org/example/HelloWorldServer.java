package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.example.GreeterGrpc;

import java.io.IOException;
import java.util.logging.Logger;

public class HelloWorldServer {
    private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());

    private int port = 50051;
    private Server server;

    private void start() throws IOException {
        //  设置service接口
        server = ServerBuilder.forPort(port)    //绑定端口
                .addService(new GreeterImpl())  //设置service接口
                .build()    //创建服务
                .start();   //开启
        logger.info("Server started,listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("*** shutting down gRPC server since JVM is shutting down");
                HelloWorldServer.this.stop();;
                System.out.println("*** server shut down");
            }
        });
    }

    private void stop(){
        if(server != null){
            server.shutdown();
        }
    }


    // block 一直到退出程序
    private void blockUntilShutdown() throws InterruptedException {
        if(server != null){
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }

    /**
     * gRPC的过程：
     * 1.创建.proto文件，定义服务和方法请求,生成对应的Proto-Java文件和gRPC文件
     * 2.创建一个服务接口的实现类，用来服务器绑定，供客户端调用
     * 3.创建服务器通道，绑定端口和服务，用于和TCP网络套接字连接
     * 4.创建客户端通道，向服务器请求连接，拿到通道对象（存根，提供与服务器相同的方法），进行一系列操作，完成后关闭channel，释放资源
     *
     * gRPC的好处：
     * 1.使用Protobuf(高效的二进制消息格式)进行序列化，
     *  优点：
     *      a.性能好，效率高：时间开销，空间开销
     *          补充：
     *              相比XML，有编码后体积更小，编解码速度更快的优势；
     *              相比于 Json，Protobuf 有更高的转化效率，时间效率和空间效率都是 JSON 的 3-5 倍。
     *      b.支持多种编程语言
     *      c.支持向后兼容和向前兼容
     *          当客户端和服务器同时用时，客户端在协议中加入一个新字节，并不影响客户端的使用
     *      d.代码生成机制
     *          protobuf可以根据.proto文件按需生成对应语言的类
     *  缺点：
     *      a.二进制格式导致可读性差   可读性意味着可编辑，可人工校验
     *          影响开发测试的效率
     *      b.缺乏自描述
     *          一般来说，XML是自描述的，而protobuf格式则不是。 给你一段二进制格式的协议内容，不配合你写的结构体是看不出来什么作用的。
     *      c.通用性差
     *          protobuf虽然支持了大量语言的序列化和反序列化，但仍然并不是一个跨平台和语言的传输标准。
     *          在多平台消息传递中，对其他项目的兼容性并不是很好，需要做相应的适配改造工作。相比json 和 XML，通用性还是没那么好。
     *
     * 2.基于HTTP2.0（并不了解）,可以保持客户单和服务器长时间连接，基于二进制流（字节流），而不是文本流
     *      二进制框架和压缩技术是的HTTP/2协议在发送和接受方面都十分的紧凑和高效
     *      多路复用，单一TCP连接上实现了多个HTTP/2调用的复用
     * 3.服务器和客户端共享.proto文件，消息和客户端的代码可以端到端生成。
     *      服务对客户端是透明的，连接到服务器之后，就可以通过通道对象调用相关api
     *
     *
     * 问题：
     *      客户端在.proto文件中新增一个字段或者新增一个服务，服务器端需要如何热更新加载.proto文件
     * **/
}
