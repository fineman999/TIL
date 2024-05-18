package io.chan.apiservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class Controller {
  @PostMapping("/api/products/{id}")
  public ResponseEntity<String> createProduct(
      @PathVariable("id") String id, @RequestBody ApiInfo apiInfo) {
    final List<ProductVO> productVOS =
        apiInfo.apiRequests().stream().map(ApiRequestVO::productVO).toList();
    for (final ProductVO productVO : productVOS) {
        log.info("Product: {}", productVO);

    }
    String result = "product" + id + " successfully created";
    return ResponseEntity.ok(result);
  }
}
