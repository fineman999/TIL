package org.example.exam05.critical_section;

public class NonRaceConditionExample {
    private static int sharedResource = 0;
    public static void main(String[] args) {

        Thread[] incrementThreads = new Thread[100];

        for (int i = 0; i < incrementThreads.length; i++) {
            incrementThreads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    synchronized (NonRaceConditionExample.class){
                        sharedResource++; // critical section
                    }
                }
            });
            incrementThreads[i].start();
        }

        for (Thread incrementThread : incrementThreads) {
            try {
                incrementThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("expected value: 1000000");
        System.out.println("actual value: " + sharedResource);
    }
}
