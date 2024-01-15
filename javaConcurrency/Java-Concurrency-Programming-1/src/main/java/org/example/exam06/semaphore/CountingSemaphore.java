package org.example.exam06.semaphore;

public class CountingSemaphore implements CommonSemaphore {

    private int signal;

    private int permits;

    public CountingSemaphore(int permits) {
        this.signal = permits;
        this.permits = permits;
    }

    @Override
    public synchronized void acquired() {
        while (signal == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.signal--;
        System.out.println(Thread.currentThread().getName() + " acquired semaphore: " + signal);
    }

    @Override
    public synchronized void release() {
        if (signal < permits) {
            this.signal++;
            System.out.println(Thread.currentThread().getName() + " released semaphore: " + signal);
            notify();
        }
    }
}

