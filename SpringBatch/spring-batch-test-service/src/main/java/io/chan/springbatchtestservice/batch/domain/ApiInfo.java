package io.chan.springbatchtestservice.batch.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record ApiInfo(
        String url,
        List<? extends ApiRequestVO> apiRequests
) {}
