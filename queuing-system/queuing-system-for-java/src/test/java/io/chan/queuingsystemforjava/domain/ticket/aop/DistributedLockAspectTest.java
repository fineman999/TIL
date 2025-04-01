package io.chan.queuingsystemforjava.domain.ticket.aop;

import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DistributedLockAspectTest.TestConfig.class)
class DistributedLockAspectTest extends BaseIntegrationTest {

    @Autowired
    private DistributedLockTarget distributedLockTarget;

    @Autowired
    private RedissonClient redissonClient;

    @Nested
    @DisplayName("DistributedLock AOP 적용 시")
    class DistributedLockTest {

        @Test
        @DisplayName("여러 스레드에서 동시에 호출해도 한 번씩 순차적으로 실행된다")
        void distributedLock() throws InterruptedException {
            // given
            int threadCount = 100;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            AtomicInteger successCount = distributedLockTarget.getSuccessCount();

            // when
            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        distributedLockTarget.increment("testKey");
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            // then
            assertThat(successCount.get()).isEqualTo(threadCount);
            assertThat(distributedLockTarget.get()).isEqualTo(threadCount);
        }
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public DistributedLockTarget distributedLockTarget() {
            return new DistributedLockTarget();
        }
    }

    // 테스트용 타겟 클래스
    static class DistributedLockTarget {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final AtomicInteger successCount = new AtomicInteger(0);

        @DistributedLock(waitTime = 5, leaseTime = 10, key = "#key")
        public void increment(String key) {
            counter.incrementAndGet();
            successCount.incrementAndGet();
        }

        public int get() {
            return counter.get();
        }

        public AtomicInteger getSuccessCount() {
            return successCount;
        }
    }
}