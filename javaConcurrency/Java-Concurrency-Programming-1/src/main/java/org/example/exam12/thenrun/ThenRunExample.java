package org.example.exam12.thenrun;

import java.util.concurrent.CompletableFuture;

public class ThenRunExample {
    public static void main(String[] args) {
        MyService myService = new MyService();

        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 40;
        }).thenApply(s -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            int data = myService.getData();
            System.out.println(s + data);
            return s + data;
        });

        // 구분 되어 있는 2개의 작업이 실행된다.
        Integer result = cf.join();
        System.out.println("result = " + result);

        CompletableFuture<Void> cf2 = cf.thenRun(() -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
        });
        cf2.join();

        System.out.println("메인 스레드 종료");
    }

    private static class MyService {
        public int getData() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 100;
        }

        public int getData2() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 200;
        }
    }
}
