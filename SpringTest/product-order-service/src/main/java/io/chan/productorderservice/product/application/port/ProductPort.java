package io.chan.productorderservice.product.application.port;

import io.chan.productorderservice.product.domain.Product;

public interface ProductPort {
    void save(Product product);

    Product getById(Long productId);
}
