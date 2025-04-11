package io.chan.queuingsystemforjava.domain.payment.service;

import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentApiClient paymentApiClient;

    public PaymentService(PaymentApiClient paymentApiClient) {
        this.paymentApiClient = paymentApiClient;
    }

    @Retry(name = "paymentRetry", fallbackMethod = "fallback")
    public PaymentResponse confirmPayment(String paymentKey, String orderId, long amount) {
        PaymentRequest request = new PaymentRequest(paymentKey, orderId, amount);
        try {
            return paymentApiClient.confirmPayment(request);
        } catch (Exception e) {
            return fallback(e);
        }
    }

    private PaymentResponse fallback(Throwable t) {
        throw new RuntimeException("Payment confirmation failed after retries: " + t.getMessage(), t);
    }
}