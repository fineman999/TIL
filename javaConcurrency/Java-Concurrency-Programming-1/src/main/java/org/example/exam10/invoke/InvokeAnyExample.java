package org.example.exam10.invoke;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class InvokeAnyExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Callable<Integer>> tasks = new ArrayList<>();

        tasks.add(() -> {
            System.out.println("비동기 작업 1 수행 중...");
            Thread.sleep(3000);
            System.out.println("비동기 작업 1 완료");
            return 1;
        });

        tasks.add(() -> {
            System.out.println("비동기 작업 2 수행 중...");
            Thread.sleep(2000);
            System.out.println("비동기 작업 2 완료");
            return 2;
        });

        tasks.add(() -> {
            System.out.println("비동기 작업 3 수행 중...");
            throw new RuntimeException("비동기 작업 3 예외 발생");
        });


        long starteTime = System.currentTimeMillis();

        try {
            // 예외가 발생하지 않는 작업의 결과를 가져옴
            int result = executorService.invokeAny(tasks);
            System.out.println("비동기 작업 결과: " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }  finally {
            long endTime = System.currentTimeMillis();
            System.out.println("작업 시간: " + (endTime - starteTime) + "ms");
            executorService.shutdown();
        }
    }
}
