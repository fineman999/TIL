package org.example.exam11.rejectedexecutionhandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiscardPolicyExample {
    public static void main(String[] args) {
        int corePoolSize = 2;
        int maxPoolSize = 2;
        long keepAliveTime = 0L;

        int workQueueCapacity = 2;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(workQueueCapacity),
                new ThreadPoolExecutor.DiscardOldestPolicy() // 작업 큐에서 가장 오래된 작업을 제거하고 새로운 작업을 추가
        ); // default

        submitTasks(executor);
    }

    private static void submitTasks(ThreadPoolExecutor executor) {
        int taskNum = 6;

        for (int i = 0; i < taskNum; i++) {
            final int taskId = i + 1;
            Runnable task = () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " 작업 완료: " + taskId + "번");
            };
            executor.submit(task);
        }

        executor.shutdown();
    }
}
