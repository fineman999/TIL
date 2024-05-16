package io.chan.springbatchtestservice.batch.domain;

import lombok.Builder;

@Builder
public record ProductVO(
        Long id,
        String name,
        int price,
        String type
) {}
