package com.grpc.store.demo;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StoreServer {

    private static final Logger log = LoggerFactory.getLogger(StoreServer.class.getName());

    private final int port;
    private final Server server;

    public static void main(String[] args) throws Exception{
        StoreServer storeServer = new StoreServer(8980);
        storeServer.start();
        if (storeServer.server != null){
            storeServer.server.awaitTermination();
        }
    }

    public StoreServer(int port) {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(new StoreService())
                .build();
    }

    private void start() throws IOException {
        server.start();
        log.info("Server started, listening on " + port);
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    System.err.println("*** shutting down gRPC server since JVM is shutting down");
                    try {
                        StoreServer.this.stop();
                    }catch (InterruptedException e){
                        e.printStackTrace(System.err);
                    }
                    System.err.println("*** server shut down");
                }));

    }
    public void stop() throws InterruptedException {
        if (server != null){
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}
