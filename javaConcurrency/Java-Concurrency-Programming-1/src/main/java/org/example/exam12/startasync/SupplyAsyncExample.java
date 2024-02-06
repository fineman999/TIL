package org.example.exam12.startasync;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SupplyAsyncExample {
    public static void main(String[] args) {

        MyService myService = new MyService();

        CompletableFuture<List<Integer>> cf = CompletableFuture.supplyAsync(new Supplier<List<Integer>>() {
            @Override
            public List<Integer> get() {
                System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
                return myService.getData();
            }
        });

        List<Integer> results = cf.join();
        results.forEach(System.out::println);

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

