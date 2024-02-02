package org.example.exam11.threadpoolsize;

import java.util.concurrent.*;

public class PrestartThreadsExample {
    public static void main(String[] args) {
        int corePoolSize = 2; // 최소 스레드 개수
        int maxPoolSize = 4; // 최대 스레드 개수
        long keepAliveTime = 0L; // 코어 스레드 이외의 스레드가 유휴 상태로 대기하는 시간
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(); // 작업 큐

        int taskNum = 9;

        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

//        executor.prestartAllCoreThreads(); // 코어 스레드를 미리 생성

//        executor.prestartCoreThread(); // 코어 스레드를 하나 생성

        for (int i = 0; i < taskNum; i++) {
            final int taskId = i + 1;
            Runnable task = () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " 작업 완료: " + taskId + "번");
            };
            executor.submit(task);
        }

        executor.shutdown();
    }
}
