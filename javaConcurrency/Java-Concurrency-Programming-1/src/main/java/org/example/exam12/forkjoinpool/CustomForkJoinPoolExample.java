package org.example.exam12.forkjoinpool;

import java.util.concurrent.ForkJoinPool;

public class CustomForkJoinPoolExample {
    public static void main(String[] args) {
        int[] arr = new int[100000000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        multiThread(arr);
        singleThread(arr);


    }

    private static void singleThread(int[] arr) {
        long start = System.currentTimeMillis();
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        System.out.println("최종 결과: " + sum);
        System.out.println("경과 시간: " + (System.currentTimeMillis() - start));
    }

    private static void multiThread(int[] arr) {
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        CustomRecursiveTask recursiveTask = new CustomRecursiveTask(arr, 0, arr.length);
        forkJoinPool.invoke(recursiveTask);

        System.out.println("최종 결과: " + recursiveTask.join());
        System.out.println("경과 시간: " + (System.currentTimeMillis() - start));
    }

}
