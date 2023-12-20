package org.example.exam02;

public class TimedWaitingStateThreadExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " : 스레드 실행 중 ...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        Thread.sleep(500);
        // TIMED_WAITING 상태
        System.out.printf("스레드 상태: %s\n", thread.getState());
    }
}
