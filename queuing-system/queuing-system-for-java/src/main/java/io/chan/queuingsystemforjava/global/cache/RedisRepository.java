package io.chan.queuingsystemforjava.global.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RedisRepository implements CacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOps;
    private final HashOperations<String, String, String> hashOps;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public void saveTokenWithPrefix(String token, UserToken userToken, Duration expireTime) {
        // Hash에 데이터 저장
        Map<String, String> userData = new HashMap<>();
        userData.put("id", String.valueOf(userToken.getId()));
        userData.put("email", userToken.getEmail());
        userData.put("role", userToken.getRole());
        hashOps.putAll(token, userData);

        // 만료 시간 설정
        redisTemplate.expire(token, expireTime);
    }
    @Override
    public String getValue(String key) {
        return valueOps.get(key);
    }

    @Override
    public void setValue(String key, String value, int minutes) {
        valueOps.set(key, value, Duration.ofMinutes(minutes));
    }

    @Override
    public void setValue(String key, String value) {
        valueOps.set(key, value);
    }

    @Override
    public void setValue(String key, String value, Duration duration) {
        valueOps.set(key, value, duration);
    }

    @Override
    public void setValue(byte[] key, byte[] value) {
        setValue(toString(key), toString(value));
    }

    @Override
    public void setValue(byte[] key, byte[] value, Duration duration) {
        setValue(toString(key), toString(value), duration);
    }

    @Override
    public String getValue(byte[] key) {
        return getValue(toString(key));
    }

    @Override
    public void remove(byte[] key) {
        remove(toString(key));
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long setValueHashes(String key, String subKey, String value) {
        hashOps.put(key, subKey, value);
        return 1L; // Redis의 put은 항상 성공 시 1을 의미하지 않으므로, 단순히 1 반환
    }

    @Override
    public Long setValueHashes(String key, String subKey, String value, Duration duration) {
        hashOps.put(key, subKey, value);
        return Boolean.TRUE.equals(redisTemplate.expire(key, duration)) ? 1L : 0L;
    }

    @Override
    public String getValueHashes(String key, String subKey) {
        return hashOps.get(key, subKey);
    }

    @Override
    public Map<String, String> getAllValueHashes(String key) {
        return hashOps.entries(key);
    }

    @Override
    public Long removeHashes(String key, String subKey) {
        return hashOps.delete(key, subKey);
    }

    @Override
    public Long countHashes(String key) {
        return hashOps.size(key);
    }

    private String toString(byte[] bytes) {
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }
}
