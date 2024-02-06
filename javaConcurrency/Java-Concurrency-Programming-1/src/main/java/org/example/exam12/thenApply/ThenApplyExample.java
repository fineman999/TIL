package org.example.exam12.thenApply;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThenApplyExample {
    public static void main(String[] args) {

        MyService myService = new MyService();
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true); // set thread as daemon
                thread.setName("CustomThreadFactory-" + counter.getAndIncrement());
                return thread;
            }
        };

        long started = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(3, threadFactory);

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 40;
        }, executorService).thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            int data = myService.getData();
            return s + data;
        }, executorService).thenApplyAsync(s -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            int data2 = myService.getData2();
            return s + data2;
        }, executorService);

        // 2개의 UniApply가 실행
        // 첫번째 uniApply가 실행되고 나서 두번째 uniApply가 실행된다. (스택처럼 순차적으로 실행된다.: 의존적)
        // 다만 메인스레드와 별도의 스레드에서 실행된다.(비동기: 독립적)
        System.out.println(completableFuture.join());

        long end = System.currentTimeMillis();

        System.out.println("총 소요시간: " + (end - started));
        System.out.println("메인 스레드 종료");


    }

    private static class MyService {
        public int getData() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 100;
        }

        public int getData2() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 200;
        }
    }

}
