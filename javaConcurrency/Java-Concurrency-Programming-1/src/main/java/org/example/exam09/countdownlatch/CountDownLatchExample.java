package org.example.exam09.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    public static void main(String[] args) throws InterruptedException {
        int numThreads = 5;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        Thread.sleep(1000);
        startSignal.countDown();
        System.out.println("startSignal.countDown() 호출");
        System.out.println("시작 신호를 보냈습니다.");
        doneSignal.await();
        System.out.println("모든 작업이 완료되었습니다.");

    }

    private static class Worker implements Runnable {


        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        public Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                System.out.println(Thread.currentThread().getName() + " 작업을 시작합니다.");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " 작업을 완료했습니다.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                doneSignal.countDown();
            }
        }
    }
}
