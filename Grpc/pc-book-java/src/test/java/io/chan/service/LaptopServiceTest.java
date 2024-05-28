package io.chan.service;

import static org.junit.jupiter.api.Assertions.*;

import io.chan.*;
import io.chan.service.bidirectional.InMemoryRatingStore;
import io.chan.service.bidirectional.RatingStore;
import io.chan.service.clientstreaming.DiskImageStore;
import io.chan.service.clientstreaming.ImageStore;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LaptopServiceTest {
  @Rule public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private LaptopStore store;
  private ImageStore imageStore;
  private RatingStore ratingStore;
  private ManagedChannel channel;

  @BeforeEach
  void setUp() throws IOException {

    store = new InMemoryLapTopStore();
    imageStore = new DiskImageStore("test-images");
    ratingStore = new InMemoryRatingStore();

    String serverName = InProcessServerBuilder.generateName();
    grpcCleanup.register(
        InProcessServerBuilder.forName(serverName)
            .directExecutor()
            .addService(new LaptopService(store, imageStore, ratingStore))
            .build()
            .start());

    channel =
        grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
  }

  @Test
  void createLaptopWithAValidID() {
    final Generator generator = new Generator();
    final Laptop laptop = generator.newLaptop();
    CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

    LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub =
        LaptopServiceGrpc.newBlockingStub(channel);
    CreateLaptopResponse response = blockingStub.createLaptop(request);
    assertNotNull(response);
    assertEquals(laptop.getId(), response.getId());

    final Optional<Laptop> found = store.find(response.getId());
    assertTrue(found.isPresent());
  }

  @Test
  void createLaptopWithAnEmptyID() {
    final Generator generator = new Generator();
    final Laptop laptop = generator.newLaptop().toBuilder().setId("").build();
    CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

    LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub =
        LaptopServiceGrpc.newBlockingStub(channel);
    CreateLaptopResponse response = blockingStub.createLaptop(request);
    assertNotNull(response);
    assertFalse(response.getId().isEmpty());

    final Optional<Laptop> found = store.find(response.getId());
    assertTrue(found.isPresent());
  }

  @Test
  void createLaptopWithAnInvalidID() {
    final Generator generator = new Generator();
    final Laptop laptop = generator.newLaptop().toBuilder().setId("invalid").build();
    CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

    LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub =
        LaptopServiceGrpc.newBlockingStub(channel);
    Assertions.assertThrows(
        StatusRuntimeException.class,
        () -> blockingStub.createLaptop(request),
        "INVALID_ARGUMENT: invalid UUID string: invalid");
  }

  @Test
  void createLaptopWithAnAlreadyExistingID() {
    final Generator generator = new Generator();
    final Laptop laptop = generator.newLaptop();
    store.save(laptop);
    CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();

    LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub =
        LaptopServiceGrpc.newBlockingStub(channel);
    Assertions.assertThrows(
        StatusRuntimeException.class,
        () -> blockingStub.createLaptop(request),
        "ALREADY_EXISTS: laptop with ID " + laptop.getId() + " already exists");
  }

  @DisplayName("bidirectional streaming")
  @Test
  void rateLaptop() {
    final Generator generator = new Generator();
    final Laptop laptop = generator.newLaptop();
    store.save(laptop);
    LaptopServiceGrpc.LaptopServiceStub asyncStub = LaptopServiceGrpc.newStub(channel);
    RateLaptopResponseStreamObserver streamObserver = new RateLaptopResponseStreamObserver();
    StreamObserver<RateLaptopRequest> requestStreamObserver = asyncStub.rateLaptop(streamObserver);

    double[] scores = {8.0, 7.0, 9.0, 10.0};
    double[] averages = {8.0, 7.5, 8.0, 8.5};

    for (final double score : scores) {
      RateLaptopRequest request =
          RateLaptopRequest.newBuilder().setLaptopId(laptop.getId()).setScore(score).build();
      requestStreamObserver.onNext(request);
    }

    requestStreamObserver.onCompleted();
    assertNotNull(streamObserver.responses);
    assertEquals(scores.length, streamObserver.responses.size());

    for (final RateLaptopResponse response : streamObserver.responses) {
      assertEquals(laptop.getId(), response.getLaptopId());
      assertEquals(
          averages[streamObserver.responses.indexOf(response)], response.getAverageScore());
    }
  }

  private static class RateLaptopResponseStreamObserver
      implements StreamObserver<RateLaptopResponse> {
    public List<RateLaptopResponse> responses;
    public Throwable error;
    public boolean completed;

    public RateLaptopResponseStreamObserver() {
      this.responses = new ArrayList<>();
    }

    @Override
    public void onNext(final RateLaptopResponse response) {
      responses.add(response);
    }

    @Override
    public void onError(final Throwable throwable) {
      error = throwable;
    }

    @Override
    public void onCompleted() {
      completed = true;
    }
  }
}
