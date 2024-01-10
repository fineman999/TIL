package io.chan.productorderservice.order.application.port;

import io.chan.productorderservice.order.domain.Order;
import io.chan.productorderservice.product.domain.Product;

public interface OrderPort {
    Product getProductById(Long productId);

    void save(Order order);
}
