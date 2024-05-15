package io.chan.springbatchtestservice.batch.domain;

public record ProductVO(
        Long id,
        String name,
        int price,
        String type
) {}
