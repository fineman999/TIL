package io.chan.productorderservice.product;

interface ProductPort {
    void save(Product product);

    Product getById(Long productId);
}
