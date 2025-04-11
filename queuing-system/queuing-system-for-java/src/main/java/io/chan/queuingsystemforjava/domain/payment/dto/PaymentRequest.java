package io.chan.queuingsystemforjava.domain.payment.dto;

public record PaymentRequest(
        String paymentKey,
        String orderId,
        long amount
) {
}