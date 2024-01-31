package org.example.exam10.future;

import java.util.concurrent.*;

public class FutureGetExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<Integer> callableTask = () -> {
            System.out.println("비동기 작업 1 수행 중...");
            Thread.sleep(3000);
            System.out.println("비동기 작업 1 완료");
            return 42;
        };

        Future<Integer> future = executorService.submit(callableTask);
        while (!future.isDone()) {
            System.out.println("작업을 기다리는 중...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }




        try {
            int result = future.get();
            System.out.println("비동기 작업 1 결과: " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
    }
}
