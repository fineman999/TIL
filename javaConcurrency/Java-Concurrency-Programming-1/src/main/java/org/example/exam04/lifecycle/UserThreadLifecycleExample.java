package org.example.exam04.lifecycle;

public class UserThreadLifecycleExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("사용자 스레드 1 실행 중");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("사용자 스레드 1 종료");
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("사용자 스레드 2 실행 중");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("사용자 스레드 2 종료");
        });

        thread1.start();
        thread2.start();

        // 사용자 스레드가 종료될 때까지 메인 스레드를 일시 정지시킨다.
        thread1.join();
        thread2.join();

        System.out.println("메인 스레드 종료");
    }
}
