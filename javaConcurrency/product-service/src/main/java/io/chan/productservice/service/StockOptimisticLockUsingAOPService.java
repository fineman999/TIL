package io.chan.productservice.service;

import io.chan.productservice.aop.Retry;
import io.chan.productservice.domain.Stock;
import io.chan.productservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockOptimisticLockUsingAOPService implements StockService {
  private final StockRepository stockRepository;

  @Transactional
  @Retry
  @Override
  public void decrease(final Long id, final Long quantity) {
      final Stock stock = stockRepository
              .findByIdWithOptimisticLock(id)
              .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
      stock.decrease(quantity);
  }
}
