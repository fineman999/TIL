package io.chan.queuingsystemforjava.global.config;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 비동기 처리를 위한 설정 클래스
 * - @EnableAsync: Spring에서 비동기 처리를 활성화하는 어노테이션. @Async 어노테이션을 사용할 수 있게 함.
 * - @Configuration: Spring의 설정 클래스임을 나타냄. Bean 정의를 포함.
 * - @RequiredArgsConstructor: Lombok 어노테이션으로, final 필드에 대한 생성자를 자동 생성.
 * - AsyncConfigurer: 비동기 실행기와 예외 처리를 커스터마이징하기 위한 인터페이스 구현.
 *  자바 가상 스레드를 이용하므로 I/O 바운드는 스레드 풀을 사용하지 않고 사용. 그러므로 CPU 바운드 작업에 대한 스레드 풀을 설정함.
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    // 전역 비동기 예외 핸들러. @Async 메서드에서 발생하는 예외를 처리하기 위해 주입됨.
    private final GlobalAsyncExceptionHandler globalAsyncExceptionHandler;

    /**
     * CPU 바운드 작업을 위한 스레드 풀 설정
     * - CPU 집약적인 작업(예: 복잡한 계산, 대규모 데이터 처리)을 처리하기 위한 Executor.
     * - Blocking Coefficient가 낮은 작업(대기 시간이 적고 CPU 연산이 많음)에 최적화.
     */
    @Bean(name = "cpuBoundTaskExecutor")
    public ThreadPoolTaskExecutor cpuBoundTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 사용 가능한 CPU 코어 수를 가져옴 (예: 8코어 시스템이면 8).
        int numOfCores = Runtime.getRuntime().availableProcessors();

        // 기본 스레드 풀 크기를 CPU 코어 수와 동일하게 설정.
        // 이유: CPU 바운드 작업은 코어 수를 초과하면 컨텍스트 스위칭 비용이 증가하므로 코어 수에 맞춤.
        executor.setCorePoolSize(numOfCores); // 예: 8

        // 최대 스레드 풀 크기도 코어 수로 제한.
        // 이유: CPU 바운드 작업에서 스레드가 코어 수를 넘으면 성능 저하가 발생함.
        executor.setMaxPoolSize(numOfCores); // 예: 8

        // 작업 대기열 크기를 50으로 설정.
        // 이유: CPU 바운드 작업은 빠른 처리가 중요하므로 대기열을 작게 유지해 쌓이지 않도록 함.
        executor.setQueueCapacity(50);

        // 스레드 우선순위를 최고(MAX_PRIORITY)로 설정.
        // 이유: CPU 바운드 작업이 CPU를 우선적으로 사용하도록 보장.
        executor.setThreadPriority(Thread.MAX_PRIORITY);

        // 대기열이 가득 찼을 때 호출자(메인 스레드)가 작업을 직접 실행하도록 설정.
        // 이유: 시스템 과부하를 방지하고 작업 거부를 피함.
        // 추천: AbortPolicy 또는 DiscardOldestPolicy
        // 왜?
        // CPU 바운드 작업은 빠른 처리가 중요하고, 대기열이 가득 차면 오래된 작업보다 새 작업을 우선할 가능성이 높음.
        // AbortPolicy: 실패를 즉시 감지하고 호출자가 대응(재시도, 로깅 등)하도록 유도.
        // DiscardOldestPolicy: 최신 계산 결과를 우선 처리.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 스레드 이름에 접두사를 추가해 디버깅 시 구분 용이.
        executor.setThreadNamePrefix("cpu-bound-");

        // 설정을 적용하고 Executor 초기화.
        executor.initialize();
        return executor;
    }
    /**
     * 비동기 작업에서 발생하는 예외를 처리하기 위한 핸들러 설정.
     * - @Async 메서드에서 던져진 예외를 GlobalAsyncExceptionHandler로 위임.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return globalAsyncExceptionHandler;
    }
}