package io.chan.couponservice.service;

import io.chan.couponservice.repository.CouponRepository;
import io.chan.couponservice.web.dto.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceUsingRedisImpl implements CouponService{
    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public void applyCoupon(CouponRequestDto couponRequestDto) {
        Long count = couponRepository.increment();
        if (count >= 100) {
            throw new RuntimeException("오늘 쿠폰이 모두 소진되었습니다.");
        }
        couponRepository.save(couponRequestDto.toEntity());
    }
}
