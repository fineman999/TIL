package io.chan.productservice.service.redis.redisson;

import io.chan.productservice.aop.redis.DistributedLock;
import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockRedisUsingAOPService implements StockService {
  private final StockRepository stockRepository;

  @Transactional
  @DistributedLock(key = "'stock-' + #id")
  @Override
  public void decrease(final Long id, final Long quantity) {
    stockRepository
        .findById(id)
        .map(
            stock -> {
              stock.decrease(quantity);
              return stockRepository.save(stock);
            })
        .orElseThrow(() -> new RuntimeException("Stock not found"));
  }
}
