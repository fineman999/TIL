package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisWaitingLine implements WaitingLine {

    private static final String WAITING_LINE_KEY = "waiting_line::";

    private final ZSetOperations<String, String> waitingLine;

    public RedisWaitingLine(StringRedisTemplate redisTemplate) {
        this.waitingLine = redisTemplate.opsForZSet();
    }

    /**
     * 시간 복잡도: O(log N) (N은 Sorted Set의 요소 수).
     * 이미 존재하는 멤버면 스코어만 업데이트.
     */
    @Override
    public void enter(String email, long performanceId, long waitingCount) {
        waitingLine.add(getWaitingLineKey(performanceId), email, waitingCount);
    }

    private String getWaitingLineKey(long performanceId) {
        return WAITING_LINE_KEY + performanceId;
    }

    /**
     * 시간 복잡도: O(log N) (N은 Sorted Set의 요소 수).
     * 원자적 연산으로 동시성 문제 없음.
     */
    @Override
    public Set<String> pullOutMembers(long performanceId, long availableToRunning) {
        return Optional.ofNullable(
                        waitingLine.popMin(getWaitingLineKey(performanceId), availableToRunning))
                .map(set -> set.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toSet()))
                .orElseGet(Set::of);
    }
}