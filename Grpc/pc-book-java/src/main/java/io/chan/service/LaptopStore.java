package io.chan.service;

import io.chan.Laptop;

import java.util.Optional;

public interface LaptopStore {
    void save(Laptop laptop);
    Optional<Laptop> find(String id);
}
