package io.chan.productorderservice.payment;


import io.chan.productorderservice.payment.application.service.PaymentRequest;

public class PaymentSteps {
    public static PaymentRequest 주문결제요청_생성() {
        final Long orderId = 1L;
        final String cardNumber = "1234-1234-1234-1234";
        return new PaymentRequest(orderId, cardNumber);
    }
}
