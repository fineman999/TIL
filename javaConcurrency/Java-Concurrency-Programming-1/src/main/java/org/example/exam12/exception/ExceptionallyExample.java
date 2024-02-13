package org.example.exam12.exception;

import java.util.concurrent.CompletableFuture;

public class ExceptionallyExample {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
                throw new IllegalArgumentException("예외 발생");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 100;
        })
        .thenApply(s -> s + 100)
        .exceptionally(e -> {
            System.out.println("Exception: " +e.getMessage());
            return -1;
        }).thenAccept(s -> System.out.println("최종 결과: " + s)).join();
    }
}
