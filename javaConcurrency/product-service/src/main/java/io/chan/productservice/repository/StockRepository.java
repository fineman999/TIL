package io.chan.productservice.repository;

import io.chan.productservice.domain.Stock;
import io.micrometer.observation.ObservationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
