package io.chan.productservice.repository;

import io.chan.productservice.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {}
