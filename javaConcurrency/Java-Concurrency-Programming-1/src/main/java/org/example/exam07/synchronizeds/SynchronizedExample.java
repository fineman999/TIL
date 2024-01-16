package org.example.exam07.synchronizeds;

public class SynchronizedExample {

    private int instanceCount = 0;
    private static int staticCount = 0;

    public synchronized void instanceMethod() {
        instanceCount++;
        System.out.println("인스턴스 메서드 동기화: " + instanceCount);
    }

    public static synchronized void staticMethod() {
        staticCount++;
        System.out.println("정적 메서드 동기화: " + staticCount);
    }

    public void instanceBlock() {
        synchronized (this) {
            instanceCount++;
            System.out.println("인스턴스 블록 동기화: " + instanceCount);
        }
    }

    public static void staticBlock() {
        synchronized (SynchronizedExample.class) {
            staticCount++;
            System.out.println("정적 블록 동기화: " + staticCount);
        }
    }

    public static void main(String[] args) {

        SynchronizedExample example = new SynchronizedExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                example.instanceMethod();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                example.instanceBlock();
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                SynchronizedExample.staticMethod();
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                SynchronizedExample.staticBlock();
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
