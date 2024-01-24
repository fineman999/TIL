package org.example.exam08.read_write_lock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        SharedData data = new SharedData();
        Thread readThread = new Thread(() -> {
            lock.readLock().lock();
            try {
                System.out.println("읽기 스레드가 데이터를 읽고 있습니다. data : " + data.getData());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("읽기 스레드가 인터럽트를 받았습니다.");
                }
            } finally {
                System.out.println("스레드 1이 읽기 락을 해제했습니다.");
                lock.readLock().unlock();
            }
        });

        Thread writeThread = new Thread(() -> {
            lock.writeLock().lock();
            try {
                System.out.println("쓰기 스레드가 데이터를 쓰고 있습니다.");
                data.setData(40);
                System.out.println("쓰기 스레드가 데이터를 변경 완료했습니다.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("쓰기 스레드가 인터럽트를 받았습니다.");
                }
            } finally {
                System.out.println("스레드 2가 쓰기 락을 해제했습니다.");
                lock.writeLock().unlock();
            }
        });

        readThread.start();
        writeThread.start();

    }

    static class SharedData {
        private int data;

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }
}
