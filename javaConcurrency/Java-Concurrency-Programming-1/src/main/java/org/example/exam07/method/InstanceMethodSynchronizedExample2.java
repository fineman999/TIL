package org.example.exam07.method;

public class InstanceMethodSynchronizedExample2 {

    private int count = 0;
    public synchronized void increment() {
        count++;
//        System.out.println(Thread.currentThread().getName() + "가 증가시켰습니다. count: " + count);
    }

    public synchronized void decrement() {
        count--;
//        System.out.println(Thread.currentThread().getName() + "가 감소시켰습니다. count: " + count);
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        InstanceMethodSynchronizedExample2 counter1 = new InstanceMethodSynchronizedExample2();
        InstanceMethodSynchronizedExample2 counter2 = new InstanceMethodSynchronizedExample2();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                counter1.increment();
                counter2.decrement();
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                counter1.decrement();
                counter2.increment();
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

        System.out.println("counter1: " + counter1.getCount());
        System.out.println("counter2: " + counter2.getCount());
    }
}
