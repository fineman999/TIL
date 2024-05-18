package io.chan.springbatchtestservice.service;

import io.chan.springbatchtestservice.batch.domain.ApiInfo;
import io.chan.springbatchtestservice.batch.domain.ApiResponseVO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ApiServiceA extends AbstractApiService {
  private final RestClient restClient;

  protected ApiServiceA(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.baseUrl("http://localhost:8081").build();
  }

  @Override
  protected ApiResponseVO doApiService(final ApiInfo apiInfo) {
    final ResponseEntity<String> responseEntity =
        this.restClient
            .post()
            .uri("/api/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(apiInfo)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                (request, response) -> {
                  throw new RuntimeException("4xx error");
                })
            .toEntity(String.class);
    return ApiResponseVO.builder()
        .status(responseEntity.getStatusCode().value())
        .message(responseEntity.getBody())
        .build();
  }
}
