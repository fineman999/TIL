package io.chan.apiservice;
import lombok.Builder;

@Builder
public record ApiRequestVO(
        Long id,
        ProductVO productVO
) {}
