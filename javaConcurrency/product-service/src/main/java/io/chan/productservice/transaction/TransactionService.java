package io.chan.productservice.transaction;

import io.chan.productservice.service.StockBasicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final StockBasicService stockService;

    // 문제 발생
    //
    public void decreaseStock(Long id, Long quantity) {
        startTransaction();
        stockService.decrease(id, quantity);
        endTransaction();
    }

    private void endTransaction() {
        log.info("Transaction ended");
    }

    private void startTransaction() {
        log.info("Transaction started");
    }


}
