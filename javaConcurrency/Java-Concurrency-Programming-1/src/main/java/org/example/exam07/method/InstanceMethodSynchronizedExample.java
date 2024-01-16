package org.example.exam07.method;

public class InstanceMethodSynchronizedExample {

    private int count = 0;
    public synchronized void increment() {
        count++;
        System.out.println(Thread.currentThread().getName() + "가 증가시켰습니다. count: " + count);
    }

    public synchronized void decrement() {
        count--;
        System.out.println(Thread.currentThread().getName() + "가 감소시켰습니다. count: " + count);
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        InstanceMethodSynchronizedExample counter = new InstanceMethodSynchronizedExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.decrement();
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.decrement();
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }
}
