package io.chan.queuingsystemforjava.domain.payment.service;

import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentConfirmationService {
    private final PaymentApiClient paymentApiClient;

    public PaymentConfirmationService(PaymentApiClient paymentApiClient) {
        this.paymentApiClient = paymentApiClient;
    }

    @Retry(name = "paymentRetry", fallbackMethod = "fallback")
    public PaymentResponse confirmPayment(String paymentKey, String orderId, BigDecimal amount) {
        PaymentRequest request = new PaymentRequest(paymentKey, orderId, amount);
        return paymentApiClient.confirmPayment(request);
    }

    public PaymentResponse fallback(String paymentKey, String orderId, BigDecimal amount, Throwable t) {
        if (t instanceof IllegalArgumentException || t instanceof IllegalStateException) {
            throw (RuntimeException) t; // 원래 예외를 그대로 던짐
        }
        throw new RuntimeException("Payment confirmation failed after retries: " + t.getMessage(), t);
    }
}