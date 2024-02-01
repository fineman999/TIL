package org.example.exam10.schedule;

import java.util.concurrent.*;

public class ScheduleCallableExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        Callable<String> task = () -> {
            return "작업이 한 번 실행되고 결과를 반환합니다.";
        };

        ScheduledFuture<String> future = scheduledThreadPool.schedule(task, 3, TimeUnit.SECONDS);

        try {
            String result = future.get();
            System.out.println("스케줄 작업 결과: " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            scheduledThreadPool.shutdown();
        }

    }
}
