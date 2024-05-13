package io.chan.couponservice.service;

import io.chan.couponservice.repository.CouponRepository;
import io.chan.couponservice.web.dto.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{
    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public void applyCoupon(CouponRequestDto couponRequestDto) {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfDay = now.atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.of(now, LocalDateTime.MAX.toLocalTime());
        Long countCoupon = couponRepository.countByRegisteredAtBetween(startOfDay, endOfDay);
        if (countCoupon >= 100) {
            throw new RuntimeException("오늘 쿠폰이 모두 소진되었습니다.");
        }

        couponRepository.save(couponRequestDto.toEntity());
    }
}
