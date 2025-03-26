package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisWaitingCounter implements WaitingCounter {

    private static final String WAITING_COUNTER_KEY = "waiting_counter::";

    private final ValueOperations<String, String> waitingCounter;

    public RedisWaitingCounter(StringRedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("redisTemplate must not be null");
        }
        this.waitingCounter = redisTemplate.opsForValue();
    }

    @Override
    public long getNextCount(long performanceId) {
        String key = getWaitingCounterKey(performanceId);
        Long incrementedValue = waitingCounter.increment(key, 1);

        // increment가 null을 반환할 경우 기본값 1로 설정
        return incrementedValue != null ? incrementedValue : 1L;
    }

    private String getWaitingCounterKey(long performanceId) {
        return WAITING_COUNTER_KEY + performanceId;
    }
}