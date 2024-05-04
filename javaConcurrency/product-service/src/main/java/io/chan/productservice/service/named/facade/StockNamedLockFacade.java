package io.chan.productservice.service.named.facade;

import io.chan.productservice.repository.NamedLockRepository;
import io.chan.productservice.service.StockService;
import io.chan.productservice.service.named.StockNamedLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockNamedLockFacade implements StockService {
  private static final int LOCK_TIMEOUT = 1000;
  private static final String LOCK_PREFIX = "stock_";
  private final NamedLockRepository namedLockRepository;
  private final StockNamedLockService stockService;

  @Override
  public void decrease(final Long id, final Long quantity) {
    namedLockRepository.executeWithLock(
        LOCK_PREFIX + id,
        LOCK_TIMEOUT,
        () -> {
          stockService.decrease(id, quantity);
          return null;
        });
  }
}
