package org.example.exam10.executor;

import java.util.concurrent.Executor;

public class AsyncExecutorExample {
    public static void main(String[] args) {
        Executor asyncExecutor = new AsyncExecutor();
        asyncExecutor.execute(() ->
        {
            System.out.println("비동기 작업 1 수행 중...");
            // ...
            System.out.println("비동기 작업 1 완료");
        });
        asyncExecutor.execute(() -> {
            System.out.println("비동기 작업 2 수행 중...");
            // ...
            System.out.println("비동기 작업 2 완료");
        });
    }

    static class AsyncExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }
}
