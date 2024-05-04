package io.chan.productservice.service.named;

import com.zaxxer.hikari.HikariDataSource;
import io.chan.productservice.domain.Stock;
import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.service.StockService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockNamedLockService implements StockService {
  private final StockRepository stockRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void decrease(final Long id, final Long quantity) {
    final Stock stock =
        stockRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
    stock.decrease(quantity);
  }
}
