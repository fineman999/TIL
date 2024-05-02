package io.chan.productservice.service;

import org.springframework.transaction.annotation.Transactional;

public interface StockService {
    void decrease(Long id, Long quantity);
}
