package org.example.exam06.semaphore;

import org.example.exam06.mutex.Mutex;
import org.example.exam06.mutex.SharedData;

public class BinarySemaphoreExample {
    public static void main(String[] args) {
        SharedResource sharedData = new SharedResource(new BinarySemaphore());

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

class SharedResource {
    private int sharedValue = 0;

    private CommonSemaphore commonSemaphore;

    public SharedResource(CommonSemaphore commonSemaphore) {
        this.commonSemaphore = commonSemaphore;
    }

    public void increment() {
        try {
            commonSemaphore.acquired();
            for (int i = 0; i < 1000000; i++) {
                sharedValue++;
            }
        } finally {
            commonSemaphore.release();
        }
    }

    public int getSharedValue() {
        return sharedValue;
    }
}
