package org.example.exam02;

public class WaitingStateThreadExample {
    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();

        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                   throw new RuntimeException(e);
                }
            }
        });

        thread.start();
        Thread.sleep(100);
        // WAITING 상태 - 다른 스레드가 통지(notify())할 때까지 기다리는 상태
        System.out.printf("스레드 상태: %s\n", thread.getState());

    }
}
