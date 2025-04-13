package io.chan.queuingsystemforjava.domain.payment.processor;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;

public interface PaymentProcessor {
    void processPayment(PaymentRequest paymentRequest);
}