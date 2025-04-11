package io.chan.queuingsystemforjava.domain.payment.adapter;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1/payments")
public interface PaymentApiClient {
    @PostExchange("/confirm")
    PaymentResponse confirmPayment(@RequestBody PaymentRequest request);
}