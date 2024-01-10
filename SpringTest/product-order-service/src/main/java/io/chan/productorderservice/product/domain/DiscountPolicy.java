package io.chan.productorderservice.product.domain;

public enum DiscountPolicy {
    NONE {
        @Override
        public int applyDiscount(final int price) {
            return price;
        }
    },
    FIX_1000_AMOUNT {
        @Override
        public int applyDiscount(final int price) {
            return Math.max(0, price - 1000);
        }
    };

    public abstract int applyDiscount(final int price);
}
