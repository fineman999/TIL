package io.chan.productorderservice.product;

import io.chan.productorderservice.ApiTest;
import io.chan.productorderservice.product.adapter.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class ProductApiTest extends ApiTest {

    @Autowired
    private ProductRepository productRepository;

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

    @Test
    void 상품_수정() {
        // 상품 등록
        ProductSteps.상품등록요청(ProductSteps.상품등록요청_생성());
        final long productId = 1L;

        final ExtractableResponse<Response> response = 상품수정요청(productId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(productRepository.findById(productId).get().getName()).isEqualTo("상품 수정");
    }

    private static ExtractableResponse<Response> 상품수정요청(final long productId) {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ProductSteps.상품수정요청())
                .when()
                .put("/products/{productId}", productId)
                .then().log().all()
                .extract();
        return response;
    }

}
