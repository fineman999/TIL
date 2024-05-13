package io.chan.couponservice.web.dto;

import io.chan.couponservice.domain.Coupon;

public record CouponRequestDto(
        String userName,
        Long price
) {
    public Coupon toEntity() {
        return Coupon.builder()
                .userName(userName)
                .price(price)
                .build();
    }
}
