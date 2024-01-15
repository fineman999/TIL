package org.example.exam06.mutex;

public class SharedData {
    private int sharedValue = 0;

    private Mutex mutex;

    public SharedData(Mutex mutex) {
        this.mutex = mutex;
    }

    public void increment() {
        try {
            mutex.acquired();
            for (int i = 0; i < 1000000; i++) {
                sharedValue++;
            }
        } finally {
            mutex.release();
        }
    }

    public int getSharedValue() {
        return sharedValue;
    }
}
