package org.example.exam01;

import java.util.ArrayList;
import java.util.List;

public class ConcurrencyExample  {
    public static void main(String[] args) {
        int cpuCores = Runtime.getRuntime().availableProcessors();

        parallelStream(cpuCores);
        basicStream(cpuCores);

    }

    private static void basicStream(int cpuCores) {
        //  CPU 개수만큼 데이터를 생성
        // 맥북 프로 2020년형 기준 8개의 CPU 코어를 가지고 있다.
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < cpuCores; i++) {
            data.add(i);
        }

        // CPU 개수만큼 데이터를 병렬로 처리
        long startTime = System.currentTimeMillis();

        // parallelStream()을 사용하면 병렬로 처리할 수 있다.
        long sum = data.stream()
                .mapToLong(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i * i;
                })
                .sum();

        long endTime = System.currentTimeMillis();

        System.out.println("CPU 개수만큼 데이터를 처리하는 데 걸리는 시간: " + (endTime - startTime) + "ms");
        System.out.printf("CPU 개수: %d\n", cpuCores);
        System.out.printf("결과: %d\n", sum);
    }

    private static void parallelStream(int cpuCores) {
        //  CPU 개수만큼 데이터를 생성
        // 맥북 프로 2020년형 기준 8개의 CPU 코어를 가지고 있다.
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < cpuCores; i++) {
            data.add(i);
        }

        // CPU 개수만큼 데이터를 병렬로 처리
        long startTime = System.currentTimeMillis();

        // parallelStream()을 사용하면 병렬로 처리할 수 있다.
        long sum = data.parallelStream()
                .mapToLong(i -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i * i;
                })
                .sum();

        long endTime = System.currentTimeMillis();

        System.out.println("CPU 개수만큼 데이터를 병렬로 처리하는 데 걸리는 시간: " + (endTime - startTime) + "ms");
        System.out.printf("CPU 개수: %d\n", cpuCores);
        System.out.printf("결과: %d\n", sum);
    }
}
