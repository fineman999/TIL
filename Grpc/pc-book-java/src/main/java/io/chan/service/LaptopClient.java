package io.chan.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import io.chan.*;
import io.chan.service.bidirectional.RateLaptopResponseStreamObserver;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class LaptopClient {
    private static final Logger logger = Logger.getLogger(LaptopClient.class.getName());
    private final ManagedChannel channel;

    private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;
    private final LaptopServiceGrpc.LaptopServiceFutureStub futureStub;
    private final LaptopServiceGrpc.LaptopServiceStub asyncStub;

    public LaptopClient(final String host, final int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
        futureStub = LaptopServiceGrpc.newFutureStub(channel);
        asyncStub = LaptopServiceGrpc.newStub(channel);
    }

    public void close() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void createLaptopSync(final Laptop laptop) {
        final CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
        CreateLaptopResponse response = CreateLaptopResponse.getDefaultInstance();

        try {
            logger.info("Sending a create laptop request with id: " + laptop.getId());
            response = blockingStub.createLaptop(request);
            logger.info("Receive a create laptop response with id: " + response.getId());
        } catch (Exception e) {
            logger.warning("RPC failed: " + e.getMessage());
        }
        logger.info("Create laptop response: " + response.getId());
    }

    public ListenableFuture<CreateLaptopResponse> createLaptopAsync(
            final Laptop laptop, Executor executor) {
        final CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

        final ListenableFuture<CreateLaptopResponse> futureResponse =
                futureStub.withDeadlineAfter(5, TimeUnit.SECONDS).createLaptop(request);
        FutureCallback<CreateLaptopResponse> callback =
                new FutureCallback<>() {
                    public void onSuccess(CreateLaptopResponse response) {
                        logger.info("Successfully created laptop with id: " + response.getId());
                    }

                    public void onFailure(Throwable t) {
                        if (!(t instanceof final StatusRuntimeException statusException)) {
                            logger.severe("Failed to create laptop: " + t.getMessage());
                            return;
                        }

                        if (!statusException.getStatus().getCode().equals(Code.ALREADY_EXISTS)) {
                            logger.severe("Failed to create laptop: " + t.getMessage());
                            return;
                        }

                        logger.info("Laptop with id already exists: " + laptop.getId());
                    }
                };
        Futures.addCallback(futureResponse, callback, executor);
        return futureResponse;
    }

    /**
     * shutdown() 호출은 채널에게 더 이상 새로운 RPC를 수락하지 않도록 지시하며, 현재 진행 중인 RPC는 계속 진행 awaitTermination() 호출은 채널이
     * 완전히 종료될 때까지 최대 5초 동안 대기합니다. 채널이 이 시간 내에 종료되면 true를 반환하고, 그렇지 않으면 false를 반환합니다. 이는 채널이 완전히 종료되기를
     * 기다리는 안전한 방법을 제공합니다.
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void uploadImage(String laptopId, String imagePath) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        final StreamObserver<UploadImageRequest> requestObserver =
                asyncStub
                        .withDeadlineAfter(5, TimeUnit.SECONDS)
                        .uploadImage(new UploadResponseStreamObserver(finishLatch));
        try (FileInputStream fileInputStream = new FileInputStream(imagePath)) {
            final String imageType = imagePath.substring(imagePath.lastIndexOf("."));
            final ImageInfo imageInfo =
                    ImageInfo.newBuilder().setLaptopId(laptopId).setImageType(imageType).build();
            // send image info
            final UploadImageRequest imageRequest =
                    UploadImageRequest.newBuilder().setInfo(imageInfo).build();

            try {
                requestObserver.onNext(imageRequest);
                logger.info("Sent image info: " + imageInfo);
                final byte[] bytes = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bytes);
                    if (read == -1) {
                        break;
                    }
                    if (finishLatch.getCount() == 0) {
                        return;
                    }
                    if (read > 0) {
                        final ByteString chunkData = ByteString.copyFrom(bytes, 0, read);
                        final UploadImageRequest chunkRequest =
                                UploadImageRequest.newBuilder().setChunkData(chunkData).build();
                        requestObserver.onNext(chunkRequest);
                        logger.info("Sent a image chunk with " + read + " bytes.");
                    }
                }
            } catch (Exception e) {
                logger.severe("Cannot upload image: " + e.getMessage());
                requestObserver.onError(e);
            }
        } catch (FileNotFoundException e) {
            logger.severe("Cannot open image file: " + e.getMessage());
        } catch (IOException e) {
            logger.severe("Cannot read image file: " + e.getMessage());
        }
        requestObserver.onCompleted();

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warning("Timeout while waiting for response");
        }
    }

    public void rateLaptop(String[] laptopIds, double[] scores) throws InterruptedException {
        CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<RateLaptopRequest> requestStreamObserver = asyncStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                .rateLaptop(new RateLaptopResponseStreamObserver(finishLatch, laptopIds, scores));

        int n = laptopIds.length;
        try {

            for (int i = 0; i < n; i++) {
                RateLaptopRequest request = RateLaptopRequest.newBuilder()
                        .setLaptopId(laptopIds[i])
                        .setScore(scores[i])
                        .build();

                requestStreamObserver.onNext(request);
                logger.info("Sent rate laptop request for laptop ID: " + laptopIds[i] + " with score: " + scores[i]);
            }
        } catch (Exception e) {
            logger.severe("Error while sending rate laptop request: " + e.getMessage());
            requestStreamObserver.onError(e);
            return;
        }

        requestStreamObserver.onCompleted();
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warning("Timeout while waiting for response");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LaptopClient client = new LaptopClient("localhost", 8081);
        final ExecutorService executor = client.newExecutor();

        Generator generator = new Generator();
        try {

//            testUploadImg(generator, client);
            testRateLaptop(generator, client);
        } finally {
            client.shutdown();
            executor.shutdown();
        }

        // async
        //    try {
        //      var futureResponse = client.createLaptopAsync(laptop, executor);
        //      futureResponse.get();
        //    } catch (Exception e) {
        //      e.printStackTrace();
        //    } finally {
        //      client.shutdown();
        //      executor.shutdown();
        //    }
        client.close();
    }

    private static void testRateLaptop(Generator generator, LaptopClient client) throws InterruptedException {
        int n = 3;
        String[] laptopIds = new String[n];

        for (int i = 0; i < n; i++) {
            Laptop laptop = generator.newLaptop();
            client.createLaptopSync(laptop);
            laptopIds[i] = laptop.getId();
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            logger.info("rate laptop (y/n)?");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("y")) {
                break;
            }

            double[] scores = new double[n];

        }
    }

    private static void testUploadImg(Generator generator, LaptopClient client) throws InterruptedException {
        //      testSearchLaptop(generator, client);

        final Laptop laptop = generator.newLaptop();
        client.createLaptopSync(laptop);
        client.uploadImage(laptop.getId(), "tmp/laptop.jpg");
    }

    private static void testSearchLaptop(final Generator generator, final LaptopClient client) {
        for (int i = 0; i < 10; i++) {
            Laptop laptop = generator.newLaptop();
            client.createLaptopSync(laptop);
        }
        final Memory minRam = Memory.newBuilder().setValue(8).setUnit(Memory.Unit.GIGABYTE).build();
        final Filter filter =
                Filter.newBuilder()
                        .setMaxPriceUsd(3000)
                        .setMinCpuCores(4)
                        .setMinCpuGhz(2.5)
                        .setMinRam(minRam)
                        .build();
        client.searchLaptop(filter);
    }

    private void searchLaptop(final Filter filter) {
        logger.info("Searching for laptops with filter: " + filter);

        final SearchLaptopRequest searchLaptopRequest =
                SearchLaptopRequest.newBuilder().setFilter(filter).build();
        blockingStub
                .withDeadlineAfter(5, TimeUnit.SECONDS) // 타임아웃 설정
                .searchLaptop(searchLaptopRequest) // 서버에 요청
                .forEachRemaining(
                        response -> {
                            logger.info("Found laptop with id: " + response.getLaptop().getId());
                        });

        logger.info("search completed.");
    }

    private ExecutorService newExecutor() {
        ThreadFactory namedThreadFactory =
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("laptop-client-thread");
                    return thread;
                };
        return Executors.newSingleThreadExecutor(namedThreadFactory);
    }
}
