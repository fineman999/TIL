package org.example.exam03;

public class CurrentThreadExample {
    public static void main(String[] args) {
        System.out.println("현재 스레드 : " + Thread.currentThread());
        System.out.println("현재 스레드 이름 : " + Thread.currentThread().getName());

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("현재 스레드 : " + Thread.currentThread());
                System.out.println("현재 스레드 이름 : " + getName());
            }
        };

        thread.start();

        Thread thread1 = new Thread(new ThreadName());
        thread1.start();
    }

    static class ThreadName implements Runnable {
        @Override
        public void run() {
            System.out.println("현재 스레드 : " + Thread.currentThread());
            System.out.println("현재 스레드 이름(Runnable 사용) : " + Thread.currentThread().getName());
        }
    }
}
