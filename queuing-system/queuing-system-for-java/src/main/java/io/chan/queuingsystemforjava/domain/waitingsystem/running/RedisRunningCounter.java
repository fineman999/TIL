package io.chan.queuingsystemforjava.domain.waitingsystem.running;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisRunningCounter implements RunningCounter {

    private static final String RUNNING_COUNTER_KEY = "running_counter:";

    private final ValueOperations<String, String> runningCounter;

    public RedisRunningCounter(StringRedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("redisTemplate must not be null");
        }
        this.runningCounter = redisTemplate.opsForValue();
    }

    @Override
    public void increment(long performanceId, int number) {
        runningCounter.increment(getRunningCounterKey(performanceId), number);
    }

    @Override
    public long getRunningCount(long performanceId) {
        String key = getRunningCounterKey(performanceId);
        // 키가 없으면 0으로 초기화
        Boolean wasSet = runningCounter.setIfAbsent(key, "0");
        String rawRunningCount = runningCounter.get(key);

        // null 체크 후 기본값 0 반환
        return rawRunningCount != null ? Long.parseLong(rawRunningCount) : 0L;
    }

    private String getRunningCounterKey(long performanceId) {
        return RUNNING_COUNTER_KEY + performanceId;
    }
}