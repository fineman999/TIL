package org.example.exam07.block;

class BankAccount {
    private double balance;

    private final Object lock = new Object();

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        synchronized (lock) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        synchronized (lock) {
            if (balance < amount) {
                return false;
            }
            balance -= amount;
            return true;
        }
    }

    // 교착상태(deadlock)를 피하기 위해 두 개의 synchronized 블록을 사용한다.
    // 두 개의 synchronized 블록을 사용할 때는 두 개의 객체의 lock을 사용해야 한다.
    public boolean transfer(BankAccount to, double amount) {
        synchronized (this.lock) {
            if (this.withdraw(amount)) {
                synchronized (to.lock) {
                    to.deposit(amount);
                    return true;
                }
            }
            return false;
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class MultipleMonitorsExample {
    public static void main(String[] args) {
        BankAccount accountA = new BankAccount(1000);
        BankAccount accountB = new BankAccount(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                boolean result = accountA.transfer(accountB, 10);
                if (result) {
                    System.out.println("A -> B 성공");
                } else {
                    System.out.println("A -> B 실패");
                }
            }
        });

        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("A 계좌 잔액: " + accountA.getBalance());
        System.out.println("B 계좌 잔액: " + accountB.getBalance());

    }
}
