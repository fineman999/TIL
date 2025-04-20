package io.chan.queuingsystemforjava.domain.payment.adapter;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1/payments")
public interface PaymentApiClient {
    @PostExchange("/confirm")
    PaymentResponse confirmPayment(@RequestBody PaymentRequest request);
    @PostExchange("/{paymentKey}/cancel")
    PaymentCancelResponse cancelPayment(
            @PathVariable String paymentKey,
            @RequestBody PaymentCancelRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey
    );
}