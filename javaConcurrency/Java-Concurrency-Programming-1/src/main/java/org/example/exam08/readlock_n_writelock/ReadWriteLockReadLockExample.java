package org.example.exam08.readlock_n_writelock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockReadLockExample {
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        BankAccount bankAccount = new BankAccount(lock);

        // 읽기 스레드가 실행되는 동안에는 쓰기 스레드가 접근할 수 없다.
        // 읽기 스레드가 실행되는 동안에는 다른 읽기 스레드가 접근할 수 있다.
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int balance = bankAccount.getBalance();
                System.out.println(Thread.currentThread().getName() + " 현재 잔액: " + balance);
            }).start();
        }


        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                int depositAmount = (int) (Math.random() * 1000);
                bankAccount.deposit(depositAmount);
                System.out.println(Thread.currentThread().getName() + " " + depositAmount + " 입금 완료");
            }).start();
        }


        // 쓰기 스레드가 실행되는 동안에는 읽기 스레드가 접근할 수 없다.
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int balance = bankAccount.getBalance();
                System.out.println(Thread.currentThread().getName() + " 현재 잔액: " + balance);
            }).start();
        }



    }
}
