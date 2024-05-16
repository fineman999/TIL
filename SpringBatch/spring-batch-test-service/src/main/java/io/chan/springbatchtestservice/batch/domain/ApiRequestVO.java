package io.chan.springbatchtestservice.batch.domain;

import lombok.Builder;
@Builder
public record ApiRequestVO(
        Long id,
        ProductVO productVO
) {}
