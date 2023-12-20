package org.example.exam02;

public class RunnableStateThreadExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                for (int i = 0; i < 100000000; i++) {
                    if (i % 10000000 == 0) {
                        // RUNNABLE 상태 - 실행 중 상태
                        System.out.printf("스레드 상태: %s : %d\n", Thread.currentThread().getState(), i);
                    }
                }
            }
        });

        thread.start();
    }
}
