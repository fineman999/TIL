package org.example.exam07.method;

public class StaticMethodSynchronizedExample {

    private static int count = 0;
    public static synchronized void increment() {
        count++;
//        System.out.println(Thread.currentThread().getName() + "가 증가시켰습니다. count: " + count);
    }

    public static synchronized void decrement() {
        count--;
//        System.out.println(Thread.currentThread().getName() + "가 감소시켰습니다. count: " + count);
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                increment();
            }
        });


        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                decrement();
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

        System.out.println("count: " + count);
    }
}
