package io.chan.productservice.service.named.facade;

import io.chan.productservice.repository.StockLockJpaRepository;
import io.chan.productservice.service.StockService;
import io.chan.productservice.service.named.StockNamedLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StockNamedLockFacade implements StockService{
    private static final int LOCK_TIMEOUT = 1000;
    private final StockLockJpaRepository stockLockJpaRepository;
    private final StockNamedLockService stockService;

    @Transactional
    @Override
    public void decrease(final Long id, final Long quantity) {
        try{
            stockLockJpaRepository.getLock(id.toString(), LOCK_TIMEOUT);
            stockService.decrease(id, quantity);
        } finally {
            stockLockJpaRepository.releaseLock(id.toString());
        }
    }
}
