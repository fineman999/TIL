package org.example.exam08.readlock_n_writelock;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public class BankAccount {
    private final ReadWriteLock lock;
    private final Map<String, Integer> balance = new java.util.HashMap<>();

    public BankAccount(ReadWriteLock lock) {
        this.lock = lock;
        balance.put("account1", 100000);
    }

    public int getBalance() {
        lock.readLock().lock();
        try {
            Thread.sleep(1000);
            return balance.get("account1");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void deposit(int amount) {
        lock.writeLock().lock();
        try {
            Thread.sleep(2000);
            balance.put("account1", balance.get("account1") + amount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void withdraw(int amount) {
        lock.writeLock().lock();
        try {
            if (balance.get("account1") < amount) {
                System.out.println(Thread.currentThread().getName() + " " + amount + " 출금 실패, 잔액 부족");
                throw new RuntimeException("잔액이 부족합니다.");
            }
            Thread.sleep(50);
            balance.put("account1", balance.get("account1") - amount);
            System.out.println(Thread.currentThread().getName() + " " + amount + " 출금 완료");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
