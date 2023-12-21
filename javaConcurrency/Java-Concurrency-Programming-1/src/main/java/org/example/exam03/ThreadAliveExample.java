package org.example.exam03;

public class ThreadAliveExample {
    public static void main(String[] args) {

        Thread task = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("현재 스레드 이름 : " + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        Thread task2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("현재 스레드 이름 : " + Thread.currentThread().getName());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        task.start();
        task2.start();
        while (task.isAlive() || task2.isAlive()) {
            System.out.println("task 스레드는 살아있음 : " + task.isAlive());
            System.out.println("task2 스레드는 살아있음 : " + task2.isAlive());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
