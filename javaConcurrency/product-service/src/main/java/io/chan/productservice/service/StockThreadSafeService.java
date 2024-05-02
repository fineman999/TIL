package io.chan.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockThreadSafeService implements StockService{
    private final StockBasicService stockService;

    @Override
    public synchronized void decrease(Long id, Long quantity) {
        stockService.decrease(id, quantity);
    }
}
