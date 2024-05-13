package io.chan.couponservice.service;

import io.chan.couponservice.web.dto.CouponRequestDto;

public interface CouponService {
    void applyCoupon(CouponRequestDto couponRequestDto);
}
