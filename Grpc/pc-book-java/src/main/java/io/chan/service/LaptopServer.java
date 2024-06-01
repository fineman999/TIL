package io.chan.service;

import io.chan.service.bidirectional.InMemoryRatingStore;
import io.chan.service.bidirectional.RatingStore;
import io.chan.service.clientstreaming.DiskImageStore;
import io.chan.service.clientstreaming.ImageStore;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaptopServer {
    private static final Logger logger = Logger.getLogger(LaptopServer.class.getName());

    private final int port;
    private final Server server;

    public LaptopServer(final int port, final Server server) {
        this.port = port;
        this.server = server;
    }

    public LaptopServer(final int port, final LaptopStore store, final ImageStore imageStore, final RatingStore ratingStore) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
            .addService(new LaptopService(store, imageStore, ratingStore))
                .addService(ProtoReflectionService.newInstance())
            .build();
    }

    public LaptopServer(final Server server, final int port) {
        this.port = port;
        this.server = server;
    }


    public void start() {
        try {
            server.start();
            logger.info("Server started, listening on " + port);
            server.awaitTermination();
        } catch (Exception e) {
            logger.severe("Failed to start the server: " + e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server since JVM is shutting down");
            try {
                LaptopServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            logger.info("Server shut down");
        }));
    }

    /**
     *  메소드는 서버가 완전히 종료될 때까지 최대 30초 동안 대기합니다.
     *  이 시간이 지나면 메소드는 반환되며, 이후에 서버가 종료되더라도 추가적인 액션은 취하지 않습니다.
     *  이 메소드는 서버의 모든 리소스가 제대로 해제되도록 보장하는 역할을 합니다.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     *  이 코드에서는 main 메소드에서 blockUntilShutdown() 메소드를 호출하므로, main 스레드가 블록됩니다.
     *  이는 서버가 계속 실행되도록 하기 위한 것입니다.
     *  서버가 종료되면 main 스레드도 종료되고, 따라서 프로그램도 종료됩니다.
     *  따라서 이 메소드는 서버가 계속 실행되도록 main 스레드를 블록하는 역할을 합니다
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int port = 8081;
        final LaptopStore laptopStore = new InMemoryLapTopStore();
        final DiskImageStore imageStore = new DiskImageStore("img");
        final InMemoryRatingStore ratingStore = new InMemoryRatingStore();
        final LaptopServer laptopServer = new LaptopServer(port, laptopStore, imageStore, ratingStore);
        laptopServer.start();
        laptopServer.blockUntilShutdown();
    }
}
