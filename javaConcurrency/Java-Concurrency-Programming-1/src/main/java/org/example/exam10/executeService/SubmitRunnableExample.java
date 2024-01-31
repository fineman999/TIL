package org.example.exam10.executeService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubmitRunnableExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<?> future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("비동기 작업 1 수행 중...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("비동기 작업 1 완료");
            }
        });

        try {
            Object object = future.get();
            System.out.println("비동기 작업 1 결과: " + object);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


        Future<String> future2 = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("비동기 작업 2 수행 중...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("비동기 작업 2 완료");
            }
        }, "result");

        try {
            String result = future2.get();
            System.out.println("비동기 작업 2 결과: " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }




        executorService.shutdown();
    }
}
