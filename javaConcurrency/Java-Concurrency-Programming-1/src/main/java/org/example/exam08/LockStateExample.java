package org.example.exam08;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStateExample {

    private static final Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("락을 한번 획득했습니다.");
                lock.lock();
                try {
                    System.out.println("락을 두번 획득했습니다.");
                } finally {
                    System.out.println("락을 한번 해제합니다.");
                    lock.unlock();
                }
            } finally {
                System.out.println("락을 두번 해제합니다.");
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("스레드 2가 락을 획득했습니다.");
            } finally {
                System.out.println("스레드 2가 락을 해제합니다.");
                lock.unlock();
            }
        }).start();
    }
}
