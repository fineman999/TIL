package io.chan.springbatchtestservice.batch.domain;

import lombok.Builder;

@Builder
public record ApiResponseVO(int status, String message) {}
