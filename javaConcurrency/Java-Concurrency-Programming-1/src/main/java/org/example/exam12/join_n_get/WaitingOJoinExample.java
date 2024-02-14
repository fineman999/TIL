package org.example.exam12.join_n_get;

import java.util.concurrent.CompletableFuture;

public class WaitingOJoinExample {
    public static void main(String[] args) {
        CompletableFuture<Void> cf1 = CompletableFuture
                .supplyAsync(() -> {
                    sleep(100);
                    System.out.println("supplyAsync: " + Thread.currentThread().getName());
                    return 100;
                }).thenApplyAsync(s -> {
                    sleep(1000);
                    System.out.println("비동기 실행 1 : " + Thread.currentThread().getName());
                    return s + 100;
                }).thenApplyAsync(s -> {
                    sleep(1000);
                    System.out.println("비동기 실행 2 : " + Thread.currentThread().getName());
                    return s + 100;
                }).thenAcceptAsync(s -> {
                    sleep(1000);
                    System.out.println("최종 결과: " + s + " : " + Thread.currentThread().getName());
                });

        cf1.join();
        System.out.println("종료");
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
