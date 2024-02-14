package org.example.exam12.forkjoinpool;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class CustomRecursiveTask extends RecursiveTask<Integer> {
    private final int[] arr;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 2;
    CustomRecursiveTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;

            // fork() 메소드를 호출하면 새로운 스레드에서 작업을 실행한다.
            // 반면 compute() 메소드를 호출하면 현재 스레드에서 작업을 실행한다.
            ForkJoinTask<Integer> left = new CustomRecursiveTask(arr, start, mid).fork();
            CustomRecursiveTask recursiveTask = new CustomRecursiveTask(arr, mid, end);

            return left.join() + recursiveTask.compute();
        }
    }
}
