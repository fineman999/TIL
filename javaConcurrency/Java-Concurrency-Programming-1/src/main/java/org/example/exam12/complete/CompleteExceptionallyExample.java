package org.example.exam12.complete;

import java.util.concurrent.CompletableFuture;

public class CompleteExceptionallyExample {
    public static void main(String[] args) {
        CompletableFuture<String> cf1 = new CompletableFuture<>();
        getData(cf1);

        CompletableFuture<String> cf2 = cf1
                .thenApply(s -> s + " + 추가 작업")
                .handle((result, e) -> {
            if (e != null) {
                System.out.println("예외 발생: " + e.getMessage());
                return "예외 발생!~";
            }
            return result;
        });

        cf2.thenAccept(s -> System.out.println("최종 결과: " + s)).join();
    }

    private static void getData(CompletableFuture<String> cf) {
        try {
            System.out.println("비동기 작업 조작");
            Thread.sleep(1000);
            throw new IllegalArgumentException("예외 발생");
        } catch (Exception e) {
            cf.completeExceptionally(e);
        }
    }
}
