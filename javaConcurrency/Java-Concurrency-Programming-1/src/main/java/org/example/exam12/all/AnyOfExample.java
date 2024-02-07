package org.example.exam12.all;

import java.util.concurrent.CompletableFuture;

public class AnyOfExample {
    public static void main(String[] args) throws InterruptedException {

        MyService sa = new MyService();
        MyService sb = new MyService();
        MyService sc = new MyService();

        long start = System.currentTimeMillis();
        CompletableFuture<Integer> cf1 = sa.getData(1000);
        CompletableFuture<Integer> cf2 = sb.getData(500);
        CompletableFuture<Integer> cf3 = sc.getData(1500);

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(cf1, cf2, cf3);

        CompletableFuture<Integer> future = anyOf.thenApply(result -> (Integer) result);

        System.out.println("result = " + future.join());

        long end = System.currentTimeMillis();
        System.out.println("총 소요시간: " + (end - start));


    }
    private static class MyService {
        public CompletableFuture<Integer> getData(int data) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
                System.out.println(Thread.currentThread().isDaemon() + ": 데몬 스레드");
                try {
                    Thread.sleep(data);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return data;
            });
        }
    }
}
