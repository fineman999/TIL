package org.hello.chapter04.item17.concurrent;

import java.util.concurrent.CountDownLatch;

public class ConcurrentExample {
    public static void main(String[] args) throws InterruptedException {
        int N = 10;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; i++) {
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        ready(); // 모든 작업자 스레드가 준비될 때까지 기다린다.
        startSignal.countDown(); // 작업자 스레드들을 깨운다.
        doneSignal.await(); // 모든 작업자 스레드가 일을 끝마치기를 기다린다.
        done();
    }

    private static void ready() {
        System.out.println("준비~~");
    }

    private static void done() {
        System.out.println("끝~~");
    }

    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                doWork();
                doneSignal.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void doWork() {
            System.out.println("작업 중~~: " + Thread.currentThread().getName());
        }
    }
}
