package org.example.exam11.keepalivetime;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KeepAliveTimeExample {
    public static void main(String[] args) throws InterruptedException {
        int corePoolSize = 2;
        int maxPoolSize = 4;
        long keepAliveTime = 1L; // 코어 스레드 이외의 스레드가 유휴 상태로 대기하는 시간
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(2));

        int taskNum = 6;

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


        executor.allowCoreThreadTimeOut(true); // 코어 스레드도 유휴 시간이 지나면 종료

        Thread.sleep(4000);
        executor.shutdown();
    }
}
