package io.chan.queuingsystemforjava.global.cache;

import java.time.Duration;
import java.util.Map;

public interface CacheRepository {

    void saveTokenWithPrefix(String token, UserToken userToken, Duration expireTime);

    String getValue(String key);

    void setValue(String key, String value, int minutes);

    void setValue(String key, String value);

    void setValue(String key, String value, Duration duration);

    void setValue(byte[] key, byte[] value);

    void setValue(byte[] key, byte[] value, Duration duration);

    String getValue(byte[] key);

    void remove(byte[] key);

    void remove(String key);

    Long setValueHashes(String key, String subKey, String value);

    Long setValueHashes(String key, String subKey, String value, Duration duration);

    String getValueHashes(String key, String subKey);

    Map<String, String> getAllValueHashes(String key);

    Long removeHashes(String key, String subKey);

    Long countHashes(String key);
}
