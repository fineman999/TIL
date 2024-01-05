package io.chan.productorderservice.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
class ProductService {

    private final ProductPort productPort;

    ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(
            @RequestBody final AddProductRequest request
    ) {
        final Product product = new Product(request.name(), request.price(), request.discountPolicy());
        productPort.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponse> getProduct(
            @PathVariable final Long productId
    ) {
        final Product product = productPort.getById(productId);
        final GetProductResponse getProductResponse = new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDiscountPolicy());

        return ResponseEntity.ok(getProductResponse);
    }

    @Transactional
    public void updateProduct(final Long productId, final UpdateProductRequest request) {
        final Product product = productPort.getById(productId);
        product.update(request.name(), request.price(), request.discountPolicy());

        productPort.save(product);
    }
}
