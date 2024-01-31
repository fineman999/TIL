package org.example.exam10.threadpool;

public class ThreadPoolMain {
    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(3);

        for (int i = 0; i < 10; i++) {
            int taskId = i;
            simpleThreadPool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ": Task " + taskId + " started");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + ": Task " + taskId + " finished");
            });
        }

        Thread.sleep(5000);
        simpleThreadPool.shutdown();
    }
}
