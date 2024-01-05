package io.chan.productorderservice.order;

import io.chan.productorderservice.product.Product;
import org.springframework.util.Assert;

class Order {
    private final Product product;
    private final int quantity;
    private Long id;

    public Order(final Product product, final int quantity) {
        Assert.notNull(product, "상품은 필수입니다.");
        Assert.isTrue(quantity > 0, "상품 수량은 0보다 커야 합니다.");
        this.product = product;
        this.quantity = quantity;

    }

    public void assignId(final Long aLong) {
        this.id = aLong;
    }

    public Long getId() {
        return id;
    }
}
