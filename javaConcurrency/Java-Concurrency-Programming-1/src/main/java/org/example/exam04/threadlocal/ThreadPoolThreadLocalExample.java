package org.example.exam04.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolThreadLocalExample {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2); // 2개의 스레드를 가진 스레드풀 생성

        // 첫 번쨰 작업: ThreadLocal 값을 설정
        executor.submit(() -> {
            threadLocal.set("작업 1의 값");

            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            // 데이터가 삭제해서 새로운 값을 받을수 있게 초기화 한다.
            threadLocal.remove();
        });

        // 잠시 대기
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 여러 번의 두 번째 작업: ThreadLocal 값을 설정하지 않고 바로 값을 가져와 출력
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            });
        }

        executor.shutdown();

    }
}
