package org.example.exam03.iterrupt;

public class InterruptExample {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            System.out.println("첫 번째 스레드 실행 중 ...");
            System.out.println("스레드 1 인터럽트 상태 : " + Thread.currentThread().isInterrupted());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("스레드 2가 스레드 1을 인터럽트 한다.");
            // thread1.interrupt()를 호출하면 thread1의 interrupted 상태가 true로 바뀐다.
            thread1.interrupt();
            System.out.println("스레드 2 인터럽트 상태 : " + Thread.currentThread().isInterrupted());
        });


        thread2.start();
        Thread.sleep(500);
        thread1.start();

        thread1.join();
        thread2.join();


        System.out.println("여기는 메인 스레드입니다.");
    }
}
