package io.chan.productorderservice.product;

import io.chan.productorderservice.product.application.service.GetProductResponse;
import io.chan.productorderservice.product.application.service.ProductService;
import io.chan.productorderservice.product.application.service.UpdateProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품수정() {
        productService.addProduct(ProductSteps.상품등록요청_생성());
        final Long productId = 1L;
        final UpdateProductRequest request = ProductSteps.상품수정요청();


        productService.updateProduct(productId, request);

        final ResponseEntity<GetProductResponse> response = productService.getProduct(productId);
        final GetProductResponse body = response.getBody();
        assertThat(body.name()).isEqualTo("상품 수정");
        assertThat(body.price()).isEqualTo(2000);

    }

}
