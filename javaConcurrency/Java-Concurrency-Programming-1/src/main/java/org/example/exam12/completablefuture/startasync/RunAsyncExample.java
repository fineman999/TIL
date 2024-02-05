package org.example.exam12.completablefuture.startasync;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RunAsyncExample {
    public static void main(String[] args) {

        MyService myService = new MyService();

        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
            myService.getData().stream().forEach(System.out::println);
        });

        cf.join(); // 결과를 기다린다.

        System.out.println("메인 스레드 종료");

    }
    private static class MyService {
        public List<Integer> getData() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return List.of(1, 2, 3, 4, 5);
        }
    }
}

