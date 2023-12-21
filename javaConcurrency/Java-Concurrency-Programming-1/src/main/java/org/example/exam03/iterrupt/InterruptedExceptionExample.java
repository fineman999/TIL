package org.example.exam03.iterrupt;

public class InterruptedExceptionExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("인터럽트 상태 1: " + Thread.currentThread().isInterrupted()); // false
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("스레드가 인터럽트 되었습니다.");
                System.out.println("인터럽트 상태 2: " + Thread.currentThread().isInterrupted()); //false
                // InterruptedException이 발생하면 interrupted 상태가 false로 바뀐다.
                // 따라서 다시 interrupted 상태를 true로 바꿔줘야 한다.
                Thread.currentThread().interrupt(); // 인터럽트 상태를 true로 바꿔준다.
            }

        });

        thread1.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread1.interrupt();
        thread1.join();
        System.out.println("인터럽트 상태 3: " + thread1.isInterrupted()); // true
    }
}
