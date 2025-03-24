package io.chan.queuingsystemforjava.domain.performance.dto;

import io.chan.queuingsystemforjava.domain.performance.Performance;

import java.time.ZonedDateTime;

public record PerformanceElement(
    Long performanceId,
    String performanceName,
    String performancePlace,
    ZonedDateTime performanceTime) {
  public static PerformanceElement of(Performance performance) {
    return new PerformanceElement(
        performance.getPerformanceId(),
        performance.getPerformanceName(),
        performance.getPerformancePlace(),
        performance.getPerformanceShowtime());
  }
}
