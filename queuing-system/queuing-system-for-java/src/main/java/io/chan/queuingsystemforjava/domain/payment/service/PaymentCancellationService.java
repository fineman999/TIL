package io.chan.queuingsystemforjava.domain.payment.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import io.chan.queuingsystemforjava.domain.payment.repository.IdempotencyRedisRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCancellationService {
    private final PaymentApiClient paymentApiClient;
    private final IdempotencyRedisRepository idempotencyRedisRepository;

    @Retry(name = "cancelRetry", fallbackMethod = "cancelFallback")
    public PaymentCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request, String idempotencyKey) {
        validateCancelRequest(request);
        validateIdempotencyKey(idempotencyKey);

        // Check if request has been processed
        PaymentCancelResponse cachedResponse = idempotencyRedisRepository.getCachedResponse(idempotencyKey);
        if (cachedResponse != null) {
            return cachedResponse; // Return cached response for idempotency
        }

        // Process new cancellation
        PaymentCancelResponse response = paymentApiClient.cancelPayment(paymentKey, request, idempotencyKey);

        // Cache the response
        idempotencyRedisRepository.cacheResponse(idempotencyKey, response);

        return response;
    }

    private void validateCancelRequest(PaymentCancelRequest request) {
        if (request.cancelReason() == null || request.cancelReason().isBlank()) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Cancel reason is required");
        }
        if (request.cancelReason().length() > 200) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Cancel reason must not exceed 200 characters");
        }
        if (request.cancelAmount() != null && request.cancelAmount().doubleValue() <= 0) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Cancel amount must be positive");
        }
        if (request.refundReceiveAccount() != null) {
            validateRefundAccount(request.refundReceiveAccount());
        }
    }

    private void validateRefundAccount(PaymentCancelRequest.RefundReceiveAccount account) {
        if (account.bank() == null || account.bank().isBlank()) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Bank code is required for refund account");
        }
        if (account.accountNumber() == null || account.accountNumber().isBlank()) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Account number is required for refund account");
        }
        if (account.holderName() == null || account.holderName().isBlank()) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Holder name is required for refund account");
        }
        if (account.accountNumber().length() > 20) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Account number must not exceed 20 characters");
        }
        if (account.holderName().length() > 60) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Holder name must not exceed 60 characters");
        }
    }

    private void validateIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Idempotency key is required");
        }
        if (idempotencyKey.length() > 300) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Idempotency key must not exceed 300 characters");
        }
    }

    public PaymentCancelResponse cancelFallback(String paymentKey, PaymentCancelRequest request, String idempotencyKey, Throwable t) {
        if (t instanceof IllegalArgumentException || t instanceof IllegalStateException || t instanceof TicketingException) {
            throw (RuntimeException) t;
        }
        throw new TicketingException(ErrorCode.PAYMENT_ERROR, "Payment cancellation failed after retries: " + t.getMessage());
    }
}