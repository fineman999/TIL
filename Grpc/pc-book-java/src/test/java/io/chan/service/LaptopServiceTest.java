package io.chan.service;

import static org.junit.jupiter.api.Assertions.*;

import io.chan.*;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import java.util.Optional;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LaptopServiceTest {
  @Rule public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private LaptopStore store;
  private ManagedChannel channel;

  @BeforeEach
  void setUp() throws IOException {

    store = new InMemoryLapTopStore();

    String serverName = InProcessServerBuilder.generateName();
    grpcCleanup.register(
        InProcessServerBuilder.forName(serverName)
            .directExecutor()
            .addService(new LaptopService(store, null,null))
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
}
