package io.chan.productservice.service;

import io.chan.productservice.domain.Stock;
import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.setup.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

  @Test
  @DisplayName("동시에 100개의 재고를 감소시킨다.")
  void decrease100() throws InterruptedException {
    int threadCount = 100;
    // 32개의 쓰레드 풀을 생성한다.
    final ExecutorService executorService = Executors.newFixedThreadPool(32);
    // 100개의 쓰레드가 모두 종료될 때까지 대기한다. - 다른 쓰레드가 종료될 때까지 대기한다.
    final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.execute(
          () -> {
            try {
              stockService.decrease(1L, 1L);
            } finally {
              countDownLatch.countDown();
            }
          });
    }
    countDownLatch.await();

    final Stock stock = stockRepository.findById(1L).get();
    // 100개의 재고를 감소시켰으므로 0이 되어야 한다.
    // 하지만 동시성 문제로 인해 재고가 0이 되지 않을 수 있다.
    // 이유는 race condition 때문이다.
    // 공유 자원에 대해 여러 쓰레드가 동시에 접근하면서 발생하는 문제이다.
    // 이때 공유 자원을 1 감소하게 되면 동시에 다른 쓰레드는 변경하지 않은 값을 읽었기 때문에 이전 값에다가 1을 빼서 저장하게 된다.
    assertThat(stock.getQuantity()).isEqualTo(0L);
  }
}
