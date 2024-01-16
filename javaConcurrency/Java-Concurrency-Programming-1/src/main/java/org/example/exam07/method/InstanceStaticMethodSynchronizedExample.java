package org.example.exam07.method;

public class InstanceStaticMethodSynchronizedExample {

    private static int staticCount = 0;
    private int count = 0;
    public synchronized void increment() {
        count++;
//        System.out.println(Thread.currentThread().getName() + "가 증가시켰습니다. count: " + count);
    }

    public synchronized void decrement() {
        count--;
//        System.out.println(Thread.currentThread().getName() + "가 감소시켰습니다. count: " + count);
    }
    public static synchronized void incrementStatic() {
        staticCount++;
//        System.out.println(Thread.currentThread().getName() + "가 증가시켰습니다. count: " + count);
    }

    public static synchronized void decrementStatic() {
        staticCount--;
//        System.out.println(Thread.currentThread().getName() + "가 감소시켰습니다. count: " + count);
    }

    public int getCount() {
        return staticCount;
    }

    public static void main(String[] args) {

        InstanceStaticMethodSynchronizedExample example = new InstanceStaticMethodSynchronizedExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                example.increment();
                incrementStatic();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                example.decrement();
                decrementStatic();
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

        System.out.println("count: " + example.getCount());
        System.out.println("staticCount: " + staticCount);
    }
}
