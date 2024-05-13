package io.chan.couponservice.repository;

import io.chan.couponservice.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CouponRepository {
    private final CouponJpaRepository couponJpaRepository;

    public Long countByRegisteredAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return couponJpaRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }

    public void save(Coupon coupon) {
        couponJpaRepository.save(coupon);
    }

    public Long getCouponCount() {
        return couponJpaRepository.count();
    }
}
