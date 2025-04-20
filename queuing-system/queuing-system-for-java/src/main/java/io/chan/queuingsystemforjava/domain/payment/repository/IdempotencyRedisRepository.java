package io.chan.queuingsystemforjava.domain.payment.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class IdempotencyRedisRepository {
    private static final String IDEMPOTENCY_PREFIX = "idempotency:";
    private static final long TTL_SECONDS = 1_296_000; // 15 days
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public String generateIdempotencyKey() {
        return UUID.randomUUID().toString();
    }

    public PaymentCancelResponse getCachedResponse(String idempotencyKey) {
        String cached = redisTemplate.opsForValue().get(IDEMPOTENCY_PREFIX + idempotencyKey);
        if (cached != null) {
            try {
                return objectMapper.readValue(cached, PaymentCancelResponse.class);
            } catch (Exception e) {
                throw new TicketingException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to parse cached response", e);
            }
        }
        return null;
    }

    public void cacheResponse(String idempotencyKey, PaymentCancelResponse response) {
        try {
            String serialized = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(
                    IDEMPOTENCY_PREFIX + idempotencyKey,
                    serialized,
                    TTL_SECONDS,
                    TimeUnit.SECONDS
            );
        } catch (Exception e) {
            throw new TicketingException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to cache response", e);
        }
    }

    public boolean hasProcessed(String idempotencyKey) {
        return redisTemplate.hasKey(IDEMPOTENCY_PREFIX + idempotencyKey);
    }
}