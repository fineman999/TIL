package org.example.exam12.complete;

import java.util.concurrent.CompletableFuture;

public class CompletedFutureExample {
    public static void main(String[] args) {
        // CompletableFuture.completedFuture() 메서드는 이미 완료된 CompletableFuture를 생성한다.
        CompletableFuture<String> cf = CompletableFuture.completedFuture("Hello, World!");

        // 위의 코드는 아래의 코드와 동일하다.
        CompletableFuture<String> cf2 = new CompletableFuture<>();
        cf2.complete("Hello, World!");


        cf.thenAccept(s -> System.out.println("최종 결과: " + s));

//        String result = cf.join();
//        System.out.println("result = " + result);

    }
}
