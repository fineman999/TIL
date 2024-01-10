package io.chan.productorderservice.product.application.service;

import io.chan.productorderservice.product.application.port.ProductPort;
import io.chan.productorderservice.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductPort productPort;

    ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }

    @Transactional
    public void addProduct(
            final AddProductRequest request
    ) {
        final Product product = new Product(request.name(), request.price(), request.discountPolicy());
        productPort.save(product);
    }

    @Transactional(readOnly = true)
    public GetProductResponse getProduct(
            final Long productId
    ) {
        final Product product = productPort.getById(productId);

        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscountPolicy());
    }

    @Transactional
    public void updateProduct(
         final Long productId,
         final UpdateProductRequest request
    ) {
        final Product product = productPort.getById(productId);
        product.update(request.name(), request.price(), request.discountPolicy());

        productPort.save(product);
    }
}
