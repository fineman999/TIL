package io.chan.couponservice.repository;

import io.chan.couponservice.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
    Long countByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
