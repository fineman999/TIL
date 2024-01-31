package org.example.exam10.future;

import java.util.concurrent.*;

public class FutureCancelExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<Integer> callableTask = () -> {
            System.out.println("비동기 작업 1 수행 중...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("비동기 작업 1 완료");
            return 42;
        };

        Future<Integer> future = executorService.submit(callableTask);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 예외 발생, 작업 취소
//        future.cancel(true);

        // 예외는 발생하지만, 작업은 계속 진행됨
        future.cancel(false);


        if (future.isCancelled()) {
            System.out.println("작업이 취소되었습니다.");
            return;
        } else if (future.isDone()) {
            System.out.println("작업이 완료되었습니다.");
        } else {
            System.out.println("작업이 진행 중입니다.");
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
