package org.example.exam12.exception;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WhenCompleteExample {
    public static void main(String[] args) {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
                throw new IllegalArgumentException("예외 발생");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 10;
        }).whenComplete((result, e) -> {
            if (e != null) {
                System.out.println("예외 발생: " + e.getMessage());
            } else {
                System.out.println("result = " + result);
            }
        });

        try {
            Thread.sleep(1000);
            Integer join = cf1.join();
            System.out.println("join = " + join);
        } catch (CompletionException e) {
            System.out.println("예외 발생: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
