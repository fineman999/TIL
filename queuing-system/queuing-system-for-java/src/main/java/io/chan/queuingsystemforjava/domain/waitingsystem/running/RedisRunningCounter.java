package io.chan.queuingsystemforjava.domain.waitingsystem.running;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Objects;

public class RedisRunningCounter implements RunningCounter {

    private static final String RUNNING_COUNTER_KEY_PREFIX = "running_counter::";

    private final ValueOperations<String, String> runningCounter;

    public RedisRunningCounter(StringRedisTemplate redisTemplate) {
        Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
        this.runningCounter = redisTemplate.opsForValue();
    }

    @Override
    public void increment(long performanceId, int number) {
        String key = getRunningCounterKey(performanceId);
        runningCounter.increment(key, number);
    }

    @Override
    public long getRunningCount(long performanceId) {
        String key = getRunningCounterKey(performanceId);
        String rawRunningCount = runningCounter.getAndSet(key, "0"); // 키가 없으면 "0" 설정 후 반환

        return parseCount(rawRunningCount);
    }

    private String getRunningCounterKey(long performanceId) {
        return RUNNING_COUNTER_KEY_PREFIX + performanceId;
    }

    private long parseCount(String rawCount) {
        if (rawCount == null) {
            return 0L;
        }
        try {
            return Long.parseLong(rawCount);
        } catch (NumberFormatException e) {
            // 로깅 추가 가능: log.warn("Invalid running count value: {}", rawCount, e);
            return 0L;
        }
    }
}