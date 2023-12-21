package org.example.exam04;

public class DefaultExceptionHandlerExample {
    public static void main(String[] args) {

        // 스레드가 비정상적으로 종료되었거나
        // 특정한 예외를 스레드 외부에서 캐치하기 위해서 자바에서는 UncaughtExceptionHandler 인터페이스를 제공
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t.getName() + " 예외 발생");
                System.out.println("예외 메시지 : " + e.getMessage());
            }
        });


        Thread thread1 = new Thread(() -> {
            throw new RuntimeException("스레드 1 예외 발생");
        });

        Thread thread2 = new Thread(() -> {
            throw new RuntimeException("스레드 2 예외 발생");
        });

        thread1.start();
        thread2.start();
    }
}
