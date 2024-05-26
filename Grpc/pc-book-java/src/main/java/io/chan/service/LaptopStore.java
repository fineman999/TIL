package io.chan.service;

import io.chan.Filter;
import io.chan.Laptop;
import io.grpc.Context;

import java.util.Optional;

public interface LaptopStore {
    void save(Laptop laptop);
    Optional<Laptop> find(String id);
    void search(Context ctx, Filter filter, LaptopStream stream);
}

