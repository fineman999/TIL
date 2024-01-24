package org.example.exam08.readlock_n_writelock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockWriteLockExample {
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        BankAccount bankAccount = new BankAccount(lock);

        new Thread(() -> {
            int balance = bankAccount.getBalance();
            System.out.println(Thread.currentThread().getName() + " 현재 잔액: " + balance);
        }).start();



        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int withdrawAmount = (int) (Math.random() * 1000);
                bankAccount.withdraw(withdrawAmount);
            }).start();
        }

        new Thread(() -> {
                int balance = bankAccount.getBalance();
                System.out.println(Thread.currentThread().getName() + " 현재 잔액: " + balance);
            }).start();

    }
}
