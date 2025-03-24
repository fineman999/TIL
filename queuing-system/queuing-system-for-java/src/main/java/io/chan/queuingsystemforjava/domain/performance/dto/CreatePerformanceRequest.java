package io.chan.queuingsystemforjava.domain.performance.dto;

import io.chan.queuingsystemforjava.domain.performance.Performance;

import java.time.ZonedDateTime;

public record CreatePerformanceRequest(
        String performanceName,
        String performanceLocation,
        ZonedDateTime performanceShowtime

) {
    public Performance toEntity() {
        return Performance.create(performanceName, performanceLocation, performanceShowtime);
    }
}
