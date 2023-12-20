package org.example.exam03.join;

public class BasicJoinExample {
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

        thread1.start();
        System.out.println("메인 스레드가 다른 스레드의 종료를 기다립니다.");

        try {
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("메인 스레드가 계속 실행됩니다.");
    }
}
