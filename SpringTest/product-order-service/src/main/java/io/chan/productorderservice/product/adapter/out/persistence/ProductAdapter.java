package io.chan.productorderservice.product.adapter.out.persistence;

import io.chan.productorderservice.product.application.port.ProductPort;
import io.chan.productorderservice.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;

    ProductAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void save(final Product product) {
        productRepository.save(product);
    }

    @Override
    public Product getById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
