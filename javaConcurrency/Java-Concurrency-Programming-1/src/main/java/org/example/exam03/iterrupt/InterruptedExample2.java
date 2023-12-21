package org.example.exam03.iterrupt;

public class InterruptedExample2 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (!Thread.interrupted()) {
                System.out.println("첫 번째 스레드 실행 중 ..." + Thread.currentThread().isInterrupted());
            }
            System.out.println("스레드 1 인터럽트 상태가 되었습니다. : " + Thread.currentThread().isInterrupted());
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("스레드 2 실행 중 ...");
                if (i == 2) {
                    thread1.interrupt();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("스레드 2 인터럽트 상태 : " + Thread.currentThread().isInterrupted());
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
