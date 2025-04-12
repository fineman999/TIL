package io.chan.queuingsystemforjava.domain.order.dto;

public record OrderResponse(
        Long id,
    String orderId,
    String status
) {
    public static OrderResponse from(Long id, String orderId, String status) {
        return new OrderResponse(id, orderId, status);
    }
}