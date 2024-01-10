package io.chan.productorderservice.product.adapter.in;


import io.chan.productorderservice.product.application.service.AddProductRequest;
import io.chan.productorderservice.product.application.service.GetProductResponse;
import io.chan.productorderservice.product.application.service.ProductService;
import io.chan.productorderservice.product.application.service.UpdateProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> addProduct(
            @RequestBody final AddProductRequest request
    ) {
        productService.addProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponse> getProduct(
            @PathVariable final Long productId
    ) {
        GetProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    @Transactional
    public ResponseEntity<Void> updateProduct(
            @PathVariable final Long productId,
            @RequestBody final UpdateProductRequest request
    ) {
        productService.updateProduct(productId, request);
        return ResponseEntity.ok().build();
    }
}
