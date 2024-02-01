package org.example.exam10.executeService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShutDownExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            executorService.submit(() -> {
                System.out.println("비동기 작업 " + index + " 수행 중...");
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " 작업 종료");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 인터럽트 상태를 true로 변경
                    throw new RuntimeException("인터럽트 발생");
                }
                System.out.println("비동기 작업 " + index + " 완료");
                return index + 42;
            });
        }

        executorService.shutdown();

        try {
            if (executorService.awaitTermination(4, TimeUnit.SECONDS)) {
                System.out.println("모든 작업 완료");
            } else {
                System.out.println("아직 작업이 남아 있습니다.");
                executorService.shutdownNow();
                System.out.println("모든 작업 강제 종료");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        if (executorService.isShutdown()) {
            System.out.println("스레드 풀 종료됨");
        } else if (executorService.isTerminated()) {
            System.out.println("모든 작업 종료됨");
        } else {
            System.out.println("아직 작업이 남아 있습니다.");
        }

        while (!executorService.isTerminated()) {
            System.out.println("아직 작업 진행 중...");
        }

        System.out.println("모든 작업이 종료되고 스레드 풀도 종료됨");
    }
}
