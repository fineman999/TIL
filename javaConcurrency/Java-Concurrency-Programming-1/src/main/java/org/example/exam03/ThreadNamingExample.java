package org.example.exam03;

public class ThreadNamingExample {
    public static void main(String[] args) throws InterruptedException {
        Thread myThread = new Thread(() -> {
            System.out.println("현재 스레드 이름 : " + Thread.currentThread().getName());
        }, "MyThread");

        myThread.start();

        Thread yourThread = new Thread(() -> {
            System.out.println("현재 스레드 이름 : " + Thread.currentThread().getName());
        });
        yourThread.setName("YourThread");
        yourThread.start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println("현재 스레드 이름 : " + Thread.currentThread().getName());
            }).start();
        }

        Thread.sleep(2000);

    }
}
