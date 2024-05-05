package io.chan.productservice.service.redis.redisson;

import io.chan.productservice.service.StockService;
import io.chan.productservice.service.redis.StockRedisService;
import jakarta.persistence.LockTimeoutException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockRedissonLockFacade implements StockService {
    private final RedissonClient redissonClient;
    private final StockRedisService stockService;
    private static final String LOCK_PREFIX = "stock:";


    @Override
    public void decrease(final Long id, final Long quantity) {
        final RLock lock = redissonClient.getLock(LOCK_PREFIX + id);
        try {
            final boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new LockTimeoutException("Failed to acquire lock");
            }
            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
