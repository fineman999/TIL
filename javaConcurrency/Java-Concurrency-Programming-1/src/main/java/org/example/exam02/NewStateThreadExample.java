package org.example.exam02;

public class NewStateThreadExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " : 스레드 실행 중 ...");
        });

        // NEW 상태 - start() 메소드가 호출되지 않은 상태
        System.out.printf("스레드 상태: %s\n", thread.getState());

    }
}
