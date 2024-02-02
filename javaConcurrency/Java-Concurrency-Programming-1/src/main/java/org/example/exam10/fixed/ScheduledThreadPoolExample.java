package org.example.exam10.fixed;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExample {
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("작업이 한 번 실행되고 결과를 반환합니다.");
        };

        int initialDelay = 0; // 초기 지연(바로 실행)
        int initialPeriod = 3; // 초기 주기(3초)
        int updatePeriod = 1; // 업데이트 주기(1초)
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(task, initialDelay, initialPeriod, TimeUnit.SECONDS);

        try {
            Thread.sleep(5000);
            future.cancel(true); // 작업 취소

            future = executorService.scheduleAtFixedRate(task, initialDelay, updatePeriod, TimeUnit.SECONDS);

            Thread.sleep(5000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            future.cancel(false);
            executorService.shutdown();
        }
    }
}
