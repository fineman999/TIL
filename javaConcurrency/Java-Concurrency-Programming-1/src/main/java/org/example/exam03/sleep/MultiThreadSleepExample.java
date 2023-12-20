package org.example.exam03.sleep;

public class MultiThreadSleepExample {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("첫 번째 스레드 실행 중 ...");
                Thread.sleep(1000);
                System.out.println("스레드 1이 깨어났습니다.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("두 번째 스레드 실행 중 ...");
                Thread.sleep(2000);
                System.out.println("스레드 2가 깨어났습니다.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        thread1.start();
        thread2.start();

        System.out.println("여기는 메인 스레드입니다.");
    }
}
