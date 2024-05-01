package io.chan.productservice.service;

import io.chan.productservice.domain.Stock;
import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.setup.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class StockServiceTest extends AcceptanceTest {

  @Autowired private StockService stockService;

  @Autowired private StockRepository stockRepository;

  @BeforeEach
  void setUp() {
    final Stock stock = Stock.builder().productId(1L).quantity(100L).build();
    stockRepository.saveAndFlush(stock);
  }

  @Test
  @DisplayName("재고를 감소시킨다.")
  void decrease() {
    stockService.decrease(1L, 1L);
    final Stock stock = stockRepository.findById(1L).get();
    // 재고가 1 감소했으므로 99가 되어야 한다.
    assertThat(stock.getQuantity()).isEqualTo(99L);
  }
}
