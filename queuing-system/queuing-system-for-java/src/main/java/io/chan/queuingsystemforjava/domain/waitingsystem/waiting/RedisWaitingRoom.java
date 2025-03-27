package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.Set;

public class RedisWaitingRoom implements WaitingRoom {

    private static final String WAITING_ROOM_KEY = "waiting_room::";

    private final HashOperations<String, String, String> waitingRoom;

    public RedisWaitingRoom(StringRedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("redisTemplate must not be null");
        }
        this.waitingRoom = redisTemplate.opsForHash();
    }

    /**
     * 정의: Hash에 필드가 존재하지 않을 때만 값을 설정합니다.
     * 용도: 중복 방지하며 데이터를 추가할 때 유용.
     * 첫 번째 호출: 성공(1 반환)
     * 두 번째 호출: 실패(0 반환)
     * 시간 복잡도: O(1).
     * 원자적 연산으로 동시성 보장.
     */
    @Override
    public boolean enter(String email, long performanceId) {
        return Boolean.TRUE.equals(waitingRoom.putIfAbsent(getWaitingRoomKey(performanceId), email, email));
    }

    /**
     * 시간 복잡도: O(1).
     * 기존 필드가 있으면 덮어씀.
     */
    @Override
    public void updateMemberInfo(String email, long performanceId, long waitingCount) {
        waitingRoom.put(getWaitingRoomKey(performanceId), email, String.valueOf(waitingCount));
    }

    private String getWaitingRoomKey(long performanceId) {
        return WAITING_ROOM_KEY + performanceId;
    }

    /**
     * 시간 복잡도: O(N) (N은 삭제할 필드 수).
     * 없는 필드는 무시.
     */
    @Override
    public void removeMemberInfo(String email, long performanceId) {
        waitingRoom.delete(getWaitingRoomKey(performanceId), email);
    }

    @Override
    public void removeMemberInfo(Set<String> emails, long performanceId) {
        if (emails.isEmpty()) {
            return;
        }
        // 명시적으로 Object...로 변환하여 varargs 호출임을 분명히 함
        Long deletedCount = waitingRoom.delete(getWaitingRoomKey(performanceId), emails.toArray(Object[]::new));
        System.out.println("Deleted count: " + deletedCount);
    }

    /**
     * 시간 복잡도: O(1).
     * 필드가 없으면 nil 반환.
     */
    @Override
    public long getMemberWaitingCount(String email, long performanceId) {
        return Optional.ofNullable(waitingRoom.get(getWaitingRoomKey(performanceId), email))
                .map(Long::parseLong)
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_WAITING_MEMBER));
    }
}