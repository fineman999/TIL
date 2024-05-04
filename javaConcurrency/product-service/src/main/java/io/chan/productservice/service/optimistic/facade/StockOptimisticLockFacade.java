package io.chan.productservice.service.optimistic.facade;

import io.chan.productservice.service.optimistic.StockOptimisticLockService;
import io.chan.productservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockOptimisticLockFacade implements StockService {
    private final StockOptimisticLockService stockOptimisticLockService;

    @Override
    public void decrease(final Long id, final Long quantity) throws InterruptedException {
        while (true) {
            try {
                stockOptimisticLockService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                if (e.getMessage().equals("Stock is not enough")) {
                    break;
                }
                // retry
                Thread.sleep(50);
            }
        }
    }
}
