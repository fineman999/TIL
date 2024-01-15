package org.example.exam06.mutex;

public class MutexExample {
    public static void main(String[] args) {
        SharedData sharedData = new SharedData(new Mutex());

        Thread t1 = new Thread(sharedData::increment);

        Thread t2 = new Thread(sharedData::increment);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("expected value: 2000000");
        System.out.println("actual value: " + sharedData.getSharedValue());
    }
}
