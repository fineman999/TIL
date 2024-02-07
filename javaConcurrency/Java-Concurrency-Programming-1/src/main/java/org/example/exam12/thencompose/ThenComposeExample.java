package org.example.exam12.thencompose;

import java.util.concurrent.CompletableFuture;

public class ThenComposeExample {
    public static void main(String[] args) {
        MyService myService = new MyService();

        long start = System.currentTimeMillis();
        CompletableFuture<Integer> cf1 = myService.getData(5);

        CompletableFuture<Integer> cf2 = cf1.thenCompose(myService::getData2);

        int finalResult = cf2.join();
        System.out.println("finalResult = " + finalResult);

        long end = System.currentTimeMillis();
        System.out.println("총 소요시간: " + (end - start));

    }

    private static class MyService {
        public CompletableFuture<Integer> getData(int data) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return data;
            });
        }

        public CompletableFuture<Integer> getData2(int data) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return data*2;
            });
        }
    }
}
