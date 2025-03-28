package io.chan.queuingsystemforjava.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 풀 크기
        executor.setMaxPoolSize(50);  // 최대 스레드 풀 크기
        executor.setQueueCapacity(100); // 대기열 크기
        executor.setThreadNamePrefix("SSE-Executor-");
        executor.initialize();
        return executor;
    }
}
