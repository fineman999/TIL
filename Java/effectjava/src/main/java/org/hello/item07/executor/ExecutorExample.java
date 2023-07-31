package org.hello.item07.executor;

import java.util.concurrent.*;

public class ExecutorExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int numberOfCpu = Runtime.getRuntime().availableProcessors();
        System.out.println("cpu: " + numberOfCpu);
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<String> submit = executorService.submit(new Task2());
        System.out.println(Thread.currentThread() + " is hello");

        System.out.println(submit.get());

        executorService.shutdown();

    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " is world");
        }
    }
    static class Task2 implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(2000L);
            return Thread.currentThread() + " is world";
        }
    }
}
