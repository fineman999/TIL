package io.chan.service;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.chan.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class LaptopClient {
  private static final Logger logger = Logger.getLogger(LaptopClient.class.getName());
  private final ManagedChannel channel;

  private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;
  private final LaptopServiceGrpc.LaptopServiceFutureStub futureStub;

  public LaptopClient(final String host, final int port) {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
    futureStub = LaptopServiceGrpc.newFutureStub(channel);
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

  public static void main(String[] args) throws InterruptedException {
    LaptopClient client = new LaptopClient("localhost", 8081);
    Generator generator = new Generator();
    Laptop laptop = generator.NewLaptop();
    final ExecutorService executor = client.newExecutor();

    try {
      var futureResponse = client.createLaptopAsync(laptop, executor);
      futureResponse.get();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      client.shutdown();
      executor.shutdown();
    }
    client.close();
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
