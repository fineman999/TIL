package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.waitingsystem.aop.WaitingAspect;
import io.chan.queuingsystemforjava.domain.waitingsystem.running.RedisRunningCounter;
import io.chan.queuingsystemforjava.domain.waitingsystem.running.RedisRunningRoom;
import io.chan.queuingsystemforjava.domain.waitingsystem.service.*;
import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingCounter;
import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingLine;
import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingRoom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class WaitingConfig {

    @Bean
    public WaitingAspect waitingAspect(WaitingSystem waitingSystem) {
        return new WaitingAspect(waitingSystem);
    }

    @Bean
    public WaitingSystem waitingSystem(
            WaitingManager waitingManager,
            RunningManager runningManager,
            EventPublisher eventPublisher) {
        return new WaitingSystem(waitingManager, runningManager, eventPublisher);
    }

    @Bean
    public WaitingManager waitingManager(
            RedisWaitingRoom waitingRoom,
            RedisWaitingLine waitingLine,
            RedisWaitingCounter waitingCounter) {
        return new RedisWaitingManager(waitingRoom, waitingCounter, waitingLine);
    }

    @Bean
    public RedisWaitingRoom waitingRoom(StringRedisTemplate redisTemplate) {
        return new RedisWaitingRoom(redisTemplate);
    }

    @Bean
    public RedisWaitingLine waitingLine(StringRedisTemplate redisTemplate) {
        return new RedisWaitingLine(redisTemplate);
    }

    @Bean
    public RedisWaitingCounter waitingCounter(StringRedisTemplate redisTemplate) {
        return new RedisWaitingCounter(redisTemplate);
    }

    @Bean
    public RunningManager runningManager(
            RedisRunningRoom runningRoom, RedisRunningCounter runningCounter) {
        return new RedisRunningManager(runningRoom, runningCounter);
    }

    @Bean
    public RedisRunningRoom runningRoom(StringRedisTemplate redisTemplate) {
        return new RedisRunningRoom(redisTemplate);
    }

    @Bean
    public RedisRunningCounter runningCounter(StringRedisTemplate redisTemplate) {
        return new RedisRunningCounter(redisTemplate);
    }
}