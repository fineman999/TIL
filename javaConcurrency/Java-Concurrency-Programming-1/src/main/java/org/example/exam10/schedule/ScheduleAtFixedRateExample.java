package org.example.exam10.schedule;

import java.util.concurrent.*;

public class ScheduleAtFixedRateExample {
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

        // 1초 후에 작업을 시작하고 이후에는 3초마다 작업을 반복한다.
        ScheduledFuture<?> scheduledFuture = scheduledThreadPool.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);

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
