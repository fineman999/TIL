package org.example.exam02;

public class BlockedStateThreadExample {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    // 무한 루프
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                System.out.printf("락을 획득하려고 시도 중 ...\n");
            }
        });

        thread1.start();
        Thread.sleep(100);
        thread2.start();
        Thread.sleep(100);

        // BLOCKED 상태 - 동기화 블록에 의해 락이 획득되지 못한 상태
        System.out.printf("스레드 상태: %s\n", thread2.getState());
    }
}
