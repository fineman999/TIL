package io.chan.productservice.service.redis.redisson;

import io.chan.productservice.service.StockService;
import io.chan.productservice.service.redis.StockRedisService;
import jakarta.persistence.LockTimeoutException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockRedissonLockFacade implements StockService {
  private final RedissonClient redissonClient;
  private final StockRedisService stockService;
  private static final String LOCK_PREFIX = "stock:";
  private static final int WAIT_TIME = 15;
  private static final int LEASE_TIME = 1;

  @Override
  public void decrease(final Long id, final Long quantity) {
    final RLock lock = redissonClient.getLock(LOCK_PREFIX + id);
    try {
      final boolean available = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
      if (!available) {
        throw new LockTimeoutException("Failed to acquire lock");
      }
      stockService.decrease(id, quantity);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Thread was interrupted while waiting for lock", e);
    } finally {
      if (lock.isLocked() && lock.isHeldByCurrentThread()) {
        lock.unlock();
      } else {
        log.warn("Lock was not acquired by current thread");
      }
    }
  }
}
