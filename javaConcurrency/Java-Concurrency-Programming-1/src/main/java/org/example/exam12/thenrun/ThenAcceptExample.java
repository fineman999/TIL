package org.example.exam12.thenrun;

import java.util.concurrent.CompletableFuture;

public class ThenAcceptExample {
    public static void main(String[] args) {
        MyService myService = new MyService();

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 시작");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 40;
        }).thenAccept(s -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            int data = myService.getData();
            System.out.println(s + data);
        }).thenAcceptAsync(s -> {
            System.out.println(Thread.currentThread().getName() + " 비동기 작업 조작");
            int data2 = myService.getData2();
            System.out.printf("최종 결과: %d\n", data2);
        }).join();
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
