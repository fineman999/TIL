package org.example.exam10.executeService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class AwaitTerminationExample {
    public static void main(String[] args) {

        // 데몬 스레드는 shutdownNow() 메소드 호출과 상관없이 main 스레드가 종료되면 같이 종료된다.
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread myThread = new Thread(r, "myThread");
                myThread.setDaemon(true);
                return myThread;
            }
        });

        executorService.submit(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " 데몬 스레드 작업 중...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        executorService.shutdownNow();

//        try {
//            if (executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
//                System.out.println("모든 작업 완료");
//            } else {
//                System.out.println("아직 작업이 남아 있습니다.");
//                executorService.shutdownNow();
//                System.out.println("모든 작업 강제 종료");
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException(e);
//        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("메인 스레드 종료");
    }
}
