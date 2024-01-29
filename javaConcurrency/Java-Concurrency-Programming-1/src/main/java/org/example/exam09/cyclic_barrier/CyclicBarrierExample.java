package org.example.exam09.cyclic_barrier;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    static int[] partialSum = new int[2];
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // 55
        int numThreads = 2;

        CyclicBarrier barrier = new CyclicBarrier(numThreads, new BarrierAction(partialSum));

        for (int i = 0; i < numThreads; i++) {
            new Thread(new Worker(i, numbers, barrier, partialSum)).start();
        }
    }

    private static class BarrierAction implements Runnable {
        private final int[] partialSum1;

        public BarrierAction(int[] partialSum) {
            partialSum1 = partialSum;
        }

        @Override
        public void run() {
            int finalSum = 0;
            for (int sum : partialSum1) {
                finalSum += sum;
            }
            System.out.println("최종 합계: " + finalSum);
        }
    }

    private static class Worker implements Runnable {
        private final int id;
        private final int[] numbers;
        private final CyclicBarrier barrier;
        private final int[] partialSum1;

        public Worker(int id, int[] numbers, CyclicBarrier barrier, int[] partialSum) {
            this.id = id;
            this.numbers = numbers;
            this.barrier = barrier;
            partialSum1 = partialSum;
        }

        @Override
        public void run() {
            int start = id * (numbers.length / 2); // 0, 4
            int end = (id + 1) * (numbers.length / 2); // 4, 8
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += numbers[i];
            }

            partialSum1[id] = sum;

            try {
                barrier.await();
                System.out.println("스레드 " + id + "가 " + start + "부터 " + end + "까지 합계를 계산했습니다.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
