package org.example.exam10.runnable_n_callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallbackExample {
    interface Callback {
        void onComplete(int result);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.execute(() -> {
            System.out.println("비동기 작업 1 수행 중...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("비동기 작업 1 완료");
            int result = 42;

            Callback callback = new MyCallback();
            callback.onComplete(result);

        });
        System.out.println("비동기 작업 시작");
    }
    static class MyCallback implements Callback {
        @Override
        public void onComplete(int result) {
            System.out.println("비동기 작업 1 결과: " + result);
        }
    }
}
