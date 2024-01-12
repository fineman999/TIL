package org.example.exam05.cpu;

public class CpuNonSyncExample {

    private static int count = 0;
    private static final int ITERATIONS = 100000;
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                count++;
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                count++;
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //예상 실행 결과: 200000
        //실제 실행 결과: 141384
        System.out.printf("예상 실행 결과: %d\n", ITERATIONS * 2);
        System.out.printf("실제 실행 결과: %d\n", count);
    }
}
