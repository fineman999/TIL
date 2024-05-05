package io.chan.productservice.service.named;

import io.chan.productservice.domain.Stock;
import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.service.StockService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockNamedLockService implements StockService {
  private final StockRepository stockRepository;
  private final DataSource dataSource;

  @Transactional
  @Override
  public void decrease(final Long id, final Long quantity) {
    log.info("dataSource={}", dataSource);
    final Stock stock =
        stockRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
    stock.decrease(quantity);
  }
}
