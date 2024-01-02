package io.chan.productorderservice.product;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
class ProductRepository {
    private Long sequence = 0L;
    private final Map<Long, Product> persistence = new HashMap<>();

    public void save(final Product product) {
        product.assignId(++sequence);
        persistence.put(product.getId(), product);
    }
}
