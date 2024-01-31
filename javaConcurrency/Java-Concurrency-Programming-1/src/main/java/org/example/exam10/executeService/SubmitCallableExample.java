package org.example.exam10.executeService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubmitCallableExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("비동기 작업 1 수행 중...");
                Thread.sleep(3000);
                System.out.println("비동기 작업 1 완료");
                return "작업 1 결과";
            }
        });
//
//        try {
//            String result = future.get();
//            System.out.println("비동기 작업 1 결과: " + result);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        executorService.shutdown();
    }

}
