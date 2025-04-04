package io.chan.queuingsystemforjava.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class LettuceRepository {
    private final StringRedisTemplate redisTemplate;

    // 1. lock을 생성
    // 2. 60초가 유지되는 key는 자리번호 value는 유저 id를 생성
    public Boolean seatLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, "lock", Duration.ofMinutes(1));
    }

    public void unlock(String string) {
        redisTemplate.delete(string);
    }
}
