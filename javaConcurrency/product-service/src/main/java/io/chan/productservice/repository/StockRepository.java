package io.chan.productservice.repository;

import io.chan.productservice.domain.Stock;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockRepository {
    private final StockJpaRepository stockJpaRepository;

    public Optional<Stock> findById(final Long id) {
        return stockJpaRepository.findById(id);
    }

    public Stock save(final Stock stock) {
        return stockJpaRepository.save(stock);
    }

    public void saveAndFlush(final Stock stock) {
        stockJpaRepository.saveAndFlush(stock);
    }

    public Optional<Stock> findByIdWithPessimisticLock(final Long id) {
        return stockJpaRepository.findByIdWithPessimisticLock(id);
    }

    public Optional<Stock> findByIdWithOptimisticLock(final Long id) {
        return stockJpaRepository.findByIdWithOptimisticLock(id);
    }
}
