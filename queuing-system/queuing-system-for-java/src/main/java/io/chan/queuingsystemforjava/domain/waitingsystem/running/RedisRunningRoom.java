package io.chan.queuingsystemforjava.domain.waitingsystem.running;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisRunningRoom implements RunningRoom {

    private static final int MAX_RUNNING_ROOM_SIZE = 100;
    private static final String RUNNING_ROOM_KEY = "running_room:";
    private static final int EXPIRED_MINUTE = 5;
    private static final int MINIMUM_RUNNING_TIME = 30;

    private final ZSetOperations<String, String> runningRoom;

    public RedisRunningRoom(StringRedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("redisTemplate must not be null");
        }
        this.runningRoom = redisTemplate.opsForZSet();
    }

    @Override
    public boolean contains(String email, long performanceId) {
        return Optional.ofNullable(runningRoom.score(getRunningRoomKey(performanceId), email))
                .isPresent();
    }

    @Override
    public long getAvailableToRunning(long performanceId) {
        Long size = runningRoom.size(getRunningRoomKey(performanceId));
        // size가 null일 경우 0으로 처리
        return MAX_RUNNING_ROOM_SIZE - (size != null ? size : 0L);
    }

    @Override
    public void enter(long performanceId, Set<String> emails) {
        if (emails.isEmpty()) {
            return;
        }
        ZonedDateTime minimumRunningTime = ZonedDateTime.now().plusSeconds(MINIMUM_RUNNING_TIME);
        Set<ZSetOperations.TypedTuple<String>> collect = emails.stream()
                .map(email -> ZSetOperations.TypedTuple.of(email, (double) minimumRunningTime.toEpochSecond()))
                .collect(Collectors.toSet());
        runningRoom.add(getRunningRoomKey(performanceId), collect);
    }

    private String getRunningRoomKey(long performanceId) {
        return RUNNING_ROOM_KEY + performanceId;
    }

    @Override
    public void pullOutRunningMember(String email, long performanceId) {
        runningRoom.remove(getRunningRoomKey(performanceId), email);
    }

    @Override
    public Set<String> removeExpiredMemberInfo(long performanceId) {
        long removeRange = ZonedDateTime.now().toEpochSecond();
        String runningRoomKey = getRunningRoomKey(performanceId);
        Set<String> removedMemberEmails = runningRoom.rangeByScore(runningRoomKey, 0, removeRange);
        runningRoom.removeRangeByScore(runningRoomKey, 0, removeRange);
        return removedMemberEmails;
    }

    @Override
    public void updateRunningMemberExpiredTime(String email, long performanceId) {
        if (runningRoom.score(getRunningRoomKey(performanceId), email) != null) {
            runningRoom.add(
                    getRunningRoomKey(performanceId),
                    email,
                    ZonedDateTime.now().plusMinutes(EXPIRED_MINUTE).toEpochSecond());
        }
    }
}