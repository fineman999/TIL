package org.example.exam10.runnable_n_callable;

import java.util.concurrent.*;

public class FutureCallbackExample {
    interface Callback {
        void onComplete(int result);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<Integer> task = () -> {
            System.out.println("비동기 작업 1 수행 중...");
            Thread.sleep(3000);
            System.out.println("비동기 작업 1 완료");
            return 123;
        };

        Future<Integer> future = executorService.submit(task);
        System.out.println("비동기 작업 시작");
        registerCallback(future, result -> {
            System.out.println("비동기 작업 1 결과: " + result);
        });

        executorService.shutdown();
    }

    private static void registerCallback(Future<Integer> future, Callback callback) {
        new Thread(() -> {
            int result;
            try {
                result = future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            callback.onComplete(result);
        }).start();
    }
}
