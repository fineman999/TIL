package org.example.exam03.iterrupt;

public class InterruptedExample1 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {

            // interrupted()는 interrupted 상태를 바꾼다. -> 인트럽트 발생시 true를 통해 while문을 빠져나오면서 interrupted 상태를 false로 바꾼다.
            while (!Thread.interrupted()) {
                System.out.println("첫 번째 스레드 실행 중 ..." + Thread.currentThread().isInterrupted());
            }
            System.out.println("스레드 1 인터럽트 상태가 되었습니다. : " + Thread.currentThread().isInterrupted());
        });
        thread1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread1.interrupt();
    }
}
