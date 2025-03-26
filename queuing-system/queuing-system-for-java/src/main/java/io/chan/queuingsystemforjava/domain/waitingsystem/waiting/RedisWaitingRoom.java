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

    @Override
    public boolean enter(String email, long performanceId) {
        return Boolean.TRUE.equals(waitingRoom.putIfAbsent(getWaitingRoomKey(performanceId), email, email));
    }

    @Override
    public void updateMemberInfo(String email, long performanceId, long waitingCount) {
        waitingRoom.put(getWaitingRoomKey(performanceId), email, String.valueOf(waitingCount));
    }

    private String getWaitingRoomKey(long performanceId) {
        return WAITING_ROOM_KEY + performanceId;
    }

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
        waitingRoom.delete(getWaitingRoomKey(performanceId), emails.toArray(Object[]::new));
    }

    @Override
    public long getMemberWaitingCount(String email, long performanceId) {
        return Optional.ofNullable(waitingRoom.get(getWaitingRoomKey(performanceId), email))
                .map(Long::parseLong)
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_WAITING_MEMBER));
    }
}