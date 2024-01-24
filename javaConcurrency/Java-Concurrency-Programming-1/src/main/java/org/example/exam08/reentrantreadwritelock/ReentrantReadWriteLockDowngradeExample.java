package org.example.exam08.reentrantreadwritelock;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDowngradeExample {

    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        Lock writeLock = lock.writeLock();

        SharedData sharedData = new SharedData();

        new Thread(() -> {
            writeLock.lock();
            try {
                System.out.println("쓰기 스레드가 데이터를 쓰고 있습니다." + Thread.currentThread().getName());

                sharedData.setData((int) (Math.random() * 100));
                System.out.println("데이터 업데이트");

                readLock.lock();
                System.out.println("다운그레이드: 쓰기 락을 읽기 락으로 변경했습니다.");

                writeLock.unlock();
                System.out.println("스레드 쓰기 락을 해제했습니다." + Thread.currentThread().getName());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } finally {
                readLock.unlock();
                System.out.println("스레드 2가 읽기 락을 해제했습니다." + Thread.currentThread().getName());
            }
        }).start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                readLock.lock();
                try {
                    System.out.println("읽기 락 획득: " + Thread.currentThread().getName());
                    int data = sharedData.getData();
                    System.out.println("읽기 스레드가 데이터를 읽고 있습니다. data : " + data);
                } finally {
                    readLock.unlock();
                    System.out.println("스레드 1이 읽기 락을 해제했습니다." + Thread.currentThread().getName());
                }
            }).start();
        }
    }

    private static class SharedData {
        private int data = 0;

        public void setData(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }
    }
}
