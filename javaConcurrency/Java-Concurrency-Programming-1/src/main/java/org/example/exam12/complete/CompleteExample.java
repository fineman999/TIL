package org.example.exam12.complete;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompleteExample {
    public static void main(String[] args) {
        MyService myService = new MyService();
        CompletableFuture<Integer> cf = myService.performTask();
        cf.thenApply(s -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            return s + 100;
        });
        int result = cf.join();
        System.out.println("result = " + result);
    }
    private static class MyService {
        public CompletableFuture<Integer> performTask() {
            CompletableFuture<Integer> cf = new CompletableFuture<>();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000);
                    // 현재 값이 null이면 지정한 값을 반환한다
                    cf.complete(100);
                    // 지정된 값이 이미 설정되어 있으면 무시한다.
                    cf.complete(200);
                } catch (InterruptedException e) {
                    cf.completeExceptionally(e);
                }
            });
            executorService.shutdown();
            return cf;
        }
    }
}
