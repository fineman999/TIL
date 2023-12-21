package org.example.exam04;

public class ThreadExceptionExample {
    public static void main(String[] args) {
        try {
            new Thread(() -> {
                throw new RuntimeException("스레드 예외 발생");
            }).start();
        } catch (Exception e) {
            // 스레드에서 발생한 예외는 스레드 내부에서 처리해야 한다.
            // 스레드에서 발생한 예외를 main 스레드에서 처리할 수 없다.
            // 따라서 스레드에서 발생한 예외를 처리할 수 있는 방법이 필요하다.
            notify(e);
        }
    }

    private static void notify(Exception e) {
        System.out.println("관리자에게 알림: " + e.getMessage());
    }
}
