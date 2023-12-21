package org.example.exam04;

public class UncaughtExceptionHandlerExample {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("스레드 1 시작");
            throw new RuntimeException("스레드 예외 발생");
        });

        thread1.setUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " 예외 발생");
            System.out.println("예외 메시지 : " + e.getMessage());
        });

        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("스레드 2 시작");
            throw new RuntimeException("스레드 예외 발생");
        });

        thread2.setUncaughtExceptionHandler((t, e) -> {
            System.out.println(t.getName() + " 예외 발생입니다.");
            System.out.println("예외 메시지 : " + e.getMessage());
        });

        thread2.start();


    }
}
