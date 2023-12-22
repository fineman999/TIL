package org.example.exam04;

import java.util.concurrent.atomic.AtomicBoolean;

public class FlagThreadStopExample2 {

    // AtomicBoolean 은 스레드 간에 공유할 수 있는 boolean 변수
    // AtomicBoolean 은 변수의 값을 읽고 변경하는 작업을 원자적으로 처리한다.
    // 메인 메모리에 저장된 변수의 값을 읽고 변경하는 작업을 원자적으로 처리
    private final AtomicBoolean running = new AtomicBoolean(true);
    public static void main(String[] args) {
        new FlagThreadStopExample2().flagTest();
    }

    private void flagTest() {
        new Thread(() -> {
            int count = 0;
            while (running.get()) {
                count ++;
            }
            System.out.println("Thread 1 종료, count : " + count);
        }).start();

        new Thread(() -> {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread 2 종료");
            running.set(false);
        }).start();
    }
}
