package org.example.exam12.exception;

import java.util.concurrent.CompletableFuture;

public class HandleExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return 10;
                }).handle((result, e) -> {
                    if (e != null) {
                        System.out.println("예외 발생: " + e.getMessage());
                        return -1;
                    }
                    return result;
                });

        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(500);
                        throw new IllegalArgumentException("예외 발생");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return 100;
                }).handle((result, e) -> {
            if (e != null) {
                System.out.println("예외 발생: " + e.getMessage());
                return -1;
            }
            return result;
        });

        CompletableFuture<Integer> cf3 = cf1.thenCombine(cf2, (result1, result2) -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            if (result1 == -1 || result2 == -1) {
                return -1;
            }
            return result1 + result2;
        });

        int result = cf3.join();
        System.out.println("result = " + result);
    }
}
