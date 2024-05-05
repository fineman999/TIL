package io.chan.productservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String LOCK_PREFIX = "lock:";
    private static final String LOCK_VALUE = "lock";
    private static final long LOCK_EXPIRE = 3000L;

    public Boolean lock(Long key) {
        return redisTemplate.opsForValue().setIfAbsent(generateKey(key), LOCK_VALUE, Duration.ofMillis(LOCK_EXPIRE));
    }

    public void unlock(Long key) {
        redisTemplate.delete(generateKey(key));
    }

    private String generateKey(final Long key) {
        return LOCK_PREFIX + key;
    }
}
