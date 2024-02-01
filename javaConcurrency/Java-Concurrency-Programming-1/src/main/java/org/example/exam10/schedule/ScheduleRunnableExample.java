package org.example.exam10.schedule;

import java.util.concurrent.*;

public class ScheduleRunnableExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("작업이 한 번 실행되고 결과를 반환합니다.");
        };

        scheduledThreadPool.schedule(task, 3, TimeUnit.SECONDS);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            scheduledThreadPool.shutdown();
        }

    }
}
