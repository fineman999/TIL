package io.chan.productorderservice.product;

import io.chan.productorderservice.ApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ProductApiTest extends ApiTest {


    @Test
    void 상품_등록() {
        final var request = ProductSteps.상품등록요청_생성();

        final var response = ProductSteps.상품등록요청(request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 상품_조회() {
        // 상품 등록
        ProductSteps.상품등록요청(ProductSteps.상품등록요청_생성());
        final long productId = 1L;

        final var response = ProductSteps.상품조회요청(productId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("상품명");
    }

}
