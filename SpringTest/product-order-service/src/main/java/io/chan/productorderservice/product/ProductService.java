package io.chan.productorderservice.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ProductService {

    private final ProductPort productPort;

    ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }

    @Transactional
    public void addProduct(final AddProductRequest request) {
        final Product product = new Product(request.name(), request.price(), request.discountPolicy());
        productPort.save(product);
    }
}
