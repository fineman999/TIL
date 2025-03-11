package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class ProductionRedisConfigTest extends BaseIntegrationTest {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("RedissonClient와 LettuceRedisTemplate이 정상적으로 주입되었다.")
    void testRedissonClient() {
        assertThat(redissonClient).isNotNull();
    }

    @Test
    @DisplayName("LettuceRedisTemplate이 정상적으로 주입되었다.")
    void testLettuceRedisTemplate() {
        assertThat(redisTemplate).isNotNull();
        redisTemplate.opsForValue().set("testKey", "testValue");
        String value = redisTemplate.opsForValue().get("testKey");
        assertThat(value).isEqualTo("testValue");
    }
}
