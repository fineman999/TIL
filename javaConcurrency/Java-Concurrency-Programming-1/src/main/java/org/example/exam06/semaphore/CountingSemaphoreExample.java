package org.example.exam06.semaphore;

public class CountingSemaphoreExample {
    public static void main(String[] args) {
        int permits = 3;
        CountingSemaphore semaphore = new CountingSemaphore(permits);
        SharedResource sharedResource = new SharedResource(semaphore);

        int threadCount = 15;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {

            threads[i] = new Thread(()->
            {
                synchronized (CountingSemaphoreExample.class){
                    sharedResource.increment();
                }
            });
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("최종값: " + sharedResource.getSharedValue());

    }
}
