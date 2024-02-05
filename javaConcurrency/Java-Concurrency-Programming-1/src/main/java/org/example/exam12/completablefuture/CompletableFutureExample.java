package org.example.exam12.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int finalResult = CompletableFuture.supplyAsync(() -> {
            System.out.println("Service1 작업 완료");
            return 1;
        })
                .thenCompose(result1 -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("Service2 작업 완료");
                    return result1 + 2;
                }))
                .thenCompose(result2 -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("Service3 작업 완료");
                    return result2 + 3;
                }))
                .thenCompose(result3 -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("Service4 작업 완료");
                    return result3 + 4;
                }))
                .thenCompose(result4 -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("Service5 작업 완료");
                    return result4 + 5;
                }))
                .get();

        System.out.println("finalResult = " + finalResult);
    }

}
