package io.chan.productservice.service.optimistic;

import io.chan.productservice.domain.Stock;
import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.service.StockService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockOptimisticLockUsingResilience4jService implements StockService {
  private static final String OPTIMISTIC_LOCK_RETRY_CONFIG = "optimisticLockRetryConfig";
  private final StockRepository stockRepository;

  @Retry(name = OPTIMISTIC_LOCK_RETRY_CONFIG, fallbackMethod = "fallback")
  @Transactional
  @Override
  public void decrease(final Long id, final Long quantity) {
    final Stock stock =
        stockRepository
            .findByIdWithOptimisticLock(id)
            .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
    stock.decrease(quantity);
  }


  // 메소드 시그니처가 동일해야 함(매개변수, 리턴타입)
  private void fallback(final Long id, final Long quantity, Exception ex) {
    // retry에 전부 실패해야 fallback이 실행
    log.info("fallback! your request is failed. id: {}, quantity: {}", id, quantity);
  }
}
