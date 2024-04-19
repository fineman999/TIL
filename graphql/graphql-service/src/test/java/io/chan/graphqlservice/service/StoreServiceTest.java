package io.chan.graphqlservice.service;

import io.chan.graphqlservice.domain.Store;
import io.chan.graphqlservice.repository.StoreRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class StoreServiceTest {
    private final int threadCount = 300;
    private final String name = "store";
    private final long quantity = 1L;
    private final long initQuantity = 300L;
    private ExecutorService executorService;
    private CountDownLatch countDownLatch;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreService storeService;



    @BeforeEach
    void setUp() {
        Store store = storeRepository.save(new Store(null, name, initQuantity));

        executorService = Executors.newFixedThreadPool(threadCount);
        countDownLatch = new CountDownLatch(threadCount);
    }

    @AfterEach
    void tearDown() {
        storeRepository.deleteAll();
    }

    @DisplayName("단일 쓰레드일 경우 재고 감소 테스트")
    @Test
    void decreaseV1() {

        storeService.decreaseV1(1L, quantity);

        Store store = storeRepository.getById(1L);
        assertThat(store.getCount()).isEqualTo(initQuantity - 1);
    }
}