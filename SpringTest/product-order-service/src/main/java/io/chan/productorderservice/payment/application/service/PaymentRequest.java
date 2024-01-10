package io.chan.productorderservice.payment.application.service;

import org.springframework.util.Assert;

public record PaymentRequest(Long orderId, String cardNumber) {
    public PaymentRequest(final Long orderId, final String cardNumber) {
        this.orderId = orderId;
        this.cardNumber = cardNumber;
        Assert.notNull(orderId, "주문 ID는 필수입니다.");
        Assert.hasText(cardNumber, "카드 번호는 필수입니다.");
    }
}
