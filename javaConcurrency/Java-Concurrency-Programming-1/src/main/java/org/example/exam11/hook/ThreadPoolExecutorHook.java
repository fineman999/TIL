package org.example.exam11.hook;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorHook extends ThreadPoolExecutor {

    public ThreadPoolExecutorHook(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        System.out.println("beforeExecute - 작업 시작: " + Thread.currentThread().getName());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (t != null) {
            System.out.println("afterExecute - 작업 중 예외 발생: " + Thread.currentThread().getName() + ", " + t.getMessage());
        } else {
            System.out.println("afterExecute - 작업 완료: " + Thread.currentThread().getName());
        }
        super.afterExecute(r, t);
    }

    @Override
    protected void terminated() {
        System.out.println("terminated - 스레드 풀 종료: " + Thread.currentThread().getName());
        super.terminated();
    }

    public static void main(String[] args) {

        int corePoolSize = 2;
        int maxPoolSize = 2;
        long keepAliveTime = 0L;
        int workQueueCapacity = 2;

        ThreadPoolExecutorHook executorHook = new ThreadPoolExecutorHook(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(workQueueCapacity)
        );

        submitTasks(executorHook);

    }

    private static void submitTasks(ThreadPoolExecutorHook executor) {
        int taskNum = 5;

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
