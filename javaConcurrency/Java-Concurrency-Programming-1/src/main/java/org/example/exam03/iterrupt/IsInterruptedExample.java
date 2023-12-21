package org.example.exam03.iterrupt;

public class IsInterruptedExample {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {

            // isInterrupted()는 interrupted 상태를 바꾸지 않는다.
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("첫 번째 스레드 실행 중 ..." + Thread.currentThread().isInterrupted());
            }
            System.out.println("스레드 1 인터럽트 상태가 되었습니다. : " + Thread.currentThread().isInterrupted());
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
