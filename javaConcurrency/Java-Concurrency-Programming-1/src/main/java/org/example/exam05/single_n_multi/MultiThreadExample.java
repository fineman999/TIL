package org.example.exam05.single_n_multi;

public class MultiThreadExample {

    private static int sum = 0;
    private static final Object lock = new Object();
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                synchronized (lock) {
                    sum += i;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 500; i < 1000; i++) {
                synchronized (lock) {
                    sum += i;
                }
                try {
                    Thread.sleep(1);
                    // 여기서 예외가 발생해도 애플리케이션이 종료되지 않는다.
//                    throw new RuntimeException("예외 발생");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

        // 합계: 499500
        //소요 시간: 631ms
        System.out.printf("합계: %d\n", sum);
        System.out.printf("소요 시간: %dms\n", System.currentTimeMillis() - start);

    }
}
