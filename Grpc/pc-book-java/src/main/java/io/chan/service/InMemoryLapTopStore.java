package io.chan.service;

import io.chan.Laptop;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLapTopStore implements LaptopStore{
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
}
