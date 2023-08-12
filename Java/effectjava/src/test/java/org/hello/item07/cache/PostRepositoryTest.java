package org.hello.item07.cache;

import org.hello.chapter01.item07.cache.CacheKey;
import org.hello.chapter01.item07.cache.Post;
import org.hello.chapter01.item07.cache.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest {

    @Test
    @DisplayName("캐시를 사용한 조회")
    void name() throws InterruptedException {
        PostRepository postRepository = new PostRepository();
        postRepository.getById(1);

        assertThat(postRepository.getCache().isEmpty()).isFalse();

        System.out.println("run gc(실행된다는 보장은 없다. 하지만 실행된다.)");
        System.gc();
        System.out.println("wait for 3 seconds");
        Thread.sleep(3000L);
        System.out.println("wake up");
        assertThat(postRepository.getCache().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주기적으로 캐시를 청소한다.")
    void backgroundThread() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        PostRepository postRepository = new PostRepository();
        postRepository.getById(1);

        Runnable removeOldCache = () -> {
            System.out.println("remove old cache");
            Map<CacheKey, Post> cache = postRepository.getCache();
            Set<CacheKey> cacheKeys = cache.keySet();
            cacheKeys.stream().min(Comparator.comparing(CacheKey::getCreated))
                    .ifPresent(cacheKey -> {
                        System.out.println("remove cacheKey = " + cacheKey);
                        cache.remove(cacheKey);
                    });
        };

        System.out.println("The time is: " + System.currentTimeMillis());

        // 1초 후에 3초마다 removeOldCache를 실행한다.
        executor.scheduleAtFixedRate(removeOldCache,
                1, 3, java.util.concurrent.TimeUnit.SECONDS);

        Thread.sleep(20000L);
    }
}