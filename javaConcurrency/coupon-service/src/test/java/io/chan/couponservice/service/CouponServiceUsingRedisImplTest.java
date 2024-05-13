package io.chan.couponservice.service;

import io.chan.couponservice.repository.CouponRepository;
import io.chan.couponservice.setup.AcceptanceTest;
import io.chan.couponservice.web.dto.CouponRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class CouponServiceUsingRedisImplTest extends AcceptanceTest {
    @Autowired
    private CouponServiceUsingRedisImpl couponService;
    @Autowired private CouponRepository couponRepository;
    @Test
    @DisplayName("동시에 100 개의 쿠폰을 발급할 수 있다. - redis를 이용해 해결")
    void applyCoupon() throws InterruptedException {
        int threadCount = 100;
        // 32개의 쓰레드 풀을 생성한다.
        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 100개의 쓰레드가 모두 종료될 때까지 대기한다. - 다른 쓰레드가 종료될 때까지 대기한다.

        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(
                    () -> {
                        CouponRequestDto request = new CouponRequestDto("coupon" + System.currentTimeMillis(), (long) (1 + (Math.random() * 1000)));
                        try {
                            couponService.applyCoupon(request);
                        } finally {
                            countDownLatch.countDown();
                        }
                    });
        }
        countDownLatch.await();
        Long couponCount = couponRepository.getCouponCount();
        // 100개의 재고를 감소시켰으므로 0이 되어야 한다.
        assertThat(couponCount).isEqualTo(100L);
    }
}