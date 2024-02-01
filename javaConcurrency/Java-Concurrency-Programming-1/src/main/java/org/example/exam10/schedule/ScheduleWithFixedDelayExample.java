package org.example.exam10.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleWithFixedDelayExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

        Runnable task = () -> {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " 작업 완료");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        // 1초 후에 작업을 시작하고 이후에는 이전 작업이 종료된 시점으로부터 1초마다 작업을 반복한다.
        ScheduledFuture<?> scheduledFuture = scheduledThreadPool.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            scheduledFuture.cancel(true);
            scheduledThreadPool.shutdown();
        }
    }
}
