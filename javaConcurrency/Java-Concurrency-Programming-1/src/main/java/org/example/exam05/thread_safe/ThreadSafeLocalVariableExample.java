package org.example.exam05.thread_safe;

public class ThreadSafeLocalVariableExample {
    public void printNumbers(int plus) {
        int localSum = 0;

        for (int i = 0; i < 10; i++) {
            localSum += i;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        localSum += plus;
        System.out.println(Thread.currentThread().getName() + ": " + localSum);
    }

    public static void main(String[] args) {
        ThreadSafeLocalVariableExample example = new ThreadSafeLocalVariableExample();

        Thread thread1 = new Thread(() -> {
            example.printNumbers(20);
        });

        Thread thread2 = new Thread(() -> {
            example.printNumbers(20);
        });

        thread1.start();
        thread2.start();
    }
}
