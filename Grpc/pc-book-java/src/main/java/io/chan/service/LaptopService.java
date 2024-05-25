package io.chan.service;

import static io.grpc.Status.CANCELLED;

import io.chan.CreateLaptopRequest;
import io.chan.CreateLaptopResponse;
import io.chan.Laptop;
import io.chan.LaptopServiceGrpc;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import java.util.logging.Logger;

public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {
  private static final Logger logger = Logger.getLogger(LaptopService.class.getName());
  private final LaptopStore store;

  public LaptopService(final LaptopStore store) {
    this.store = store;
  }

  @Override
  public void createLaptop(
      final CreateLaptopRequest request,
      final StreamObserver<CreateLaptopResponse> responseObserver) {
    final Laptop laptop = request.getLaptop();
    logger.info("Receive a create laptop request with id: " + laptop.getId());

    UUID uuid;
    if (laptop.getId().isEmpty()) {
      uuid = UUID.randomUUID();
      logger.info("The laptop ID is not set. Generating a new ID: " + uuid);
    } else {
      try {
        uuid = UUID.fromString(laptop.getId());
      } catch (IllegalArgumentException e) {
        responseObserver.onError(
            io.grpc.Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        return;
      }
    }

    // heavy processing
//    try {
//      TimeUnit.SECONDS.sleep(6);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

    // gRPC에서 클라이언트 요청이 취소되었는지 확인
    // 취소 되었을 경우 적절한 응답을 보내고 메소드를 종료
    if (Context.current().isCancelled()) {
      logger.info("Request is cancelled.");
      responseObserver.onError(
          CANCELLED.withDescription("Request is cancelled.").asRuntimeException());
      return;
    }
    final Laptop other = laptop.toBuilder().setId(uuid.toString()).build();
    try {
      store.save(other);
    } catch (AlreadyExistsException e) {
      responseObserver.onError(
          io.grpc.Status.ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException());
      return;
    } catch (Exception e) {
      responseObserver.onError(
          io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
      return;
    }

    final CreateLaptopResponse response =
        CreateLaptopResponse.newBuilder().setId(other.getId()).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();

    logger.info("Laptop with ID: " + other.getId() + " is created successfully.");
  }
}
