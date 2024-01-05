package io.chan.productorderservice.order;

import io.chan.productorderservice.product.Product;

interface OrderPort {
    Product getProductById(Long productId);

    void save(Order order);
}
