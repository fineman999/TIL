package org.example.exam10.fixed;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FixedCustomThreadFactoryExample {
    public static void main(String[] args) {
        CustomThreadFactory threadFactory = new CustomThreadFactory("custom");
        ExecutorService executorService = Executors.newFixedThreadPool(3, threadFactory);

        for (int i = 0; i < 5; i++) {
            Callable<Integer> task = () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " 작업 완료");
                return 1;
            };
            executorService.submit(task);
        }

        executorService.shutdown();
    }

    static class CustomThreadFactory implements ThreadFactory {
        private final String poolName;
        private int threadCount = 0;

        public CustomThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public Thread newThread(Runnable r) {
            threadCount++;
            String threadName = poolName + "-thread-" + threadCount;
            Thread myThread = new Thread(r, threadName);
            System.out.println("새로운 스레드 생성: " + myThread.getName());
            return myThread;
        }
    }
}
