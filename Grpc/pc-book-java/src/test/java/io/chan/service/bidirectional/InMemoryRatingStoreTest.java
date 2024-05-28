package io.chan.service.bidirectional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRatingStoreTest {

    @DisplayName("add a new rating")
    @Test
    void add() {
        InMemoryRatingStore store = new InMemoryRatingStore();
        String laptopId = "1";
        double score = 5.0;

        Rating rating = store.add(laptopId, score);
        assertNotNull(rating);
        assertEquals(1, rating.count());
        assertEquals(score, rating.sum());

        Rating rating2 = store.add(laptopId, 3.0);
        assertNotNull(rating2);
        assertEquals(2, rating2.count());
        assertEquals(8.0, rating2.sum());
    }

    @DisplayName("add a new rating with a new laptop ID")
    @Test
    void addNewLaptop() throws InterruptedException {
        InMemoryRatingStore ratingStore = new InMemoryRatingStore();

        List<Callable<Rating>> tasks = new LinkedList<>();
        String laptopId = UUID.randomUUID().toString();
        double score = 5;

        int n = 10;
        for (int i = 0; i < n; i++) {
            tasks.add(() -> ratingStore.add(laptopId, score));
        }

        Set<Integer> ratedCount = new HashSet<>();
        Executors.newWorkStealingPool()
                .invokeAll(tasks)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        fail("unexpected error: " + e);
                        return null;
                    }
                })
                .forEach(rating -> {
                    assertNotNull(rating);
                    assertEquals(rating.sum(), score * rating.count());
                    ratedCount.add(rating.count());
                });

        assertEquals(n, ratedCount.size());
        for (int i = 1; i <= n; i++) {
            assertTrue(ratedCount.contains(i));
        }
    }
}