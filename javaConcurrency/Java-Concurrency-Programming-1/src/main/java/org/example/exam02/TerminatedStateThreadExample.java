package org.example.exam02;

public class TerminatedStateThreadExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.printf("스레드 상태: %s\n", Thread.currentThread().getState());
        });

        thread.start();
        thread.join();
        // TERMINATED 상태 - 스레드가 종료된 상태
        System.out.printf("스레드 상태: %s\n", thread.getState());
    }
}
