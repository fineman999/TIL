package org.example.exam03.iterrupt;

public class InterruptedExample3 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            while (true) {
                System.out.println("스레드 작동 중");
                if (Thread.interrupted()) {
                    System.out.println("인터럽트 상태가 초기화 되었습니다.: " + Thread.currentThread().isInterrupted());
                    break;
                }
            }
            System.out.println("스레드 1 종료");
            System.out.println("인터럽트 상태 : " + Thread.currentThread().isInterrupted());
            Thread.currentThread().interrupt();
            System.out.println("인터럽트 상태 : " + Thread.currentThread().isInterrupted());
        });

        thread1.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread1.interrupt();
    }
}
