package org.example.exam08.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockWithExample {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            try {
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    System.out.println("스레드 1이 락을 획득했습니다.");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.out.println("스레드 1이 인터럽트를 받았습니다.");
                    } finally {
                        lock.unlock();
                        System.out.println("스레드 1이 락을 해제했습니다.");
                    }
                } else {
                    System.out.println("스레드 1이 락을 획득하지 못했습니다.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        Thread t2 = new Thread(() -> {
            try {
                if (lock.tryLock(4, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("스레드 2가 락을 획득했습니다.");
                    } finally {
                        lock.unlock();
                        System.out.println("스레드 2가 락을 해제했습니다.");

                    }
                } else {
                    System.out.println("스레드 2가 락을 획득하지 못했습니다.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
