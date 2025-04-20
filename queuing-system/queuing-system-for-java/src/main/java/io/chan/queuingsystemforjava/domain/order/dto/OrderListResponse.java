package io.chan.queuingsystemforjava.domain.order.dto;

import io.chan.queuingsystemforjava.domain.order.Order;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderListResponse(
        String orderId,
        String orderName,
        String performanceName,
        String performancePlace,
        ZonedDateTime performanceShowtime,
        BigDecimal amount,
        String status,
        ZonedDateTime createdAt
) {
    public static OrderListResponse from(Order order) {
        return new OrderListResponse(
                order.getOrderId(),
                order.getOrderName(),
                order.getPerformance().getPerformanceName(),
                order.getPerformance().getPerformancePlace(),
                order.getPerformance().getPerformanceShowtime(),
                order.getAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}


