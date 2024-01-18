package org.example.exam08.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptiblyExample {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println("스레드 1이 락을 획득했습니다.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("스레드 1이 인터럽트를 받았습니다. - 1");
                } finally {
                    lock.unlock();
                    System.out.println("스레드 1이 락을 해제했습니다.");
                }

            } catch (InterruptedException e) {
                System.out.println("스레드 1이 인터럽트를 받았습니다.");
            }
        });


        Thread t2 = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                try {
                    System.out.println("스레드 2가 락을 획득했습니다.");
                } finally {
                    lock.unlock();
                    System.out.println("스레드 2가 락을 해제했습니다.");

                }
            } catch (InterruptedException e) {
                System.out.println("스레드 2가 인터럽트를 받았습니다.");
            }
        });

        t1.start();
        t2.start();

        t1.interrupt();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
