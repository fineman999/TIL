package io.chan.service;

import io.chan.Filter;
import io.chan.Laptop;
import io.grpc.Context;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class InMemoryLapTopStore implements LaptopStore {
  private static final Logger logger = Logger.getLogger(InMemoryLapTopStore.class.getName());
  private final ConcurrentHashMap<String, Laptop> data;

  public InMemoryLapTopStore() {
    this.data = new ConcurrentHashMap<>();
  }

  @Override
  public void save(final Laptop laptop) {
    if (data.containsKey(laptop.getId())) {
      throw new AlreadyExistsException("laptop with id already exists");
    }
    // deep copy
    Laptop deepCopy = laptop.toBuilder().build();
    data.put(deepCopy.getId(), deepCopy);
  }

  @Override
  public Optional<Laptop> find(final String id) {
    if (!data.containsKey(id)) {
      return Optional.empty();
    }
    return Optional.of(data.get(id));
  }

  @Override
  public void search(Context ctx, final Filter filter, final LaptopStream stream) {
    for (Laptop laptop : data.values()) {
      if (ctx.isCancelled()) {
        logger.info("Request is cancelled.");
        return;
      }
      // heavy processing
      //      try {
      //        TimeUnit.SECONDS.sleep(1);
      //      } catch (InterruptedException e) {
      //        e.printStackTrace();
      //      }
      if (isQualified(laptop, filter)) {
        stream.Send(laptop);
      }
    }
  }

  private boolean isQualified(final Laptop laptop, final Filter filter) {
    if (laptop.getPriceUsd() > filter.getMaxPriceUsd()) {
      return false;
    }

    if (laptop.getCpu().getNumberCores() < filter.getMinCpuCores()) {
      return false;
    }

    if (laptop.getCpu().getMinGhz() < filter.getMinCpuGhz()) {
      return false;
    }

    return true;
  }
}
