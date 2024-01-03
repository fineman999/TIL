package io.chan.productorderservice.product;

import org.springframework.util.Assert;

record GetProductResponse(
        long id,
        String name,
        int price,
        DiscountPolicy discountPolicy
) {
    public GetProductResponse(final long id, final String name, final int price, final DiscountPolicy discountPolicy) {
        Assert.notNull(id, "상품 ID는 필수입니다.");
        Assert.hasText(name, "상품명은 필수입니다.");
        Assert.isTrue(price > 0, "상품 가격은 0원보다 커야 합니다.");
        this.id = id;
        this.name = name;
        this.price = price;
        this.discountPolicy = discountPolicy;
    }
}
