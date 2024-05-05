package io.chan.productservice.service.redis.letture;

import io.chan.productservice.repository.RedisLockRepository;
import io.chan.productservice.service.StockService;
import io.chan.productservice.service.redis.StockRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockLettuceLockFacade implements StockService {
    private static final int THREAD_SLEEP = 100;
    private final RedisLockRepository redisLockRepository;
    private final StockRedisService stockService;


    @Override
    public void decrease(final Long id, final Long quantity) {
        while (!redisLockRepository.lock(id)) {
            try {
                Thread.sleep(THREAD_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }

    }
}
