package io.chan.productservice.repository;

import io.chan.productservice.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockLockJpaRepository extends JpaRepository<Stock, Long> {
  @Query(value = "select get_lock(:key, :timeout)", nativeQuery = true)
  void getLock(String key, int timeout);

  @Query(value = "select release_lock(:key)", nativeQuery = true)
  void releaseLock(String key);
}
