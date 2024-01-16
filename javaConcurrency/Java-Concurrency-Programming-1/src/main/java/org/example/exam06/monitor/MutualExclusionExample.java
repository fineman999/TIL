package org.example.exam06.monitor;

public class MutualExclusionExample {
    private int counter = 0;

    public synchronized void increment() {
        counter++;
        System.out.println(Thread.currentThread().getName() + " : " + counter);
    }
    public static void main(String[] args) {
        MutualExclusionExample mutualExclusionExample = new MutualExclusionExample();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                mutualExclusionExample.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                mutualExclusionExample.increment();
            }
        });

        thread1.start();
        thread2.start();
    }

}
