package org.example.exam11.blockingqueue;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueExample {
    public static void main(String[] args) {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i); // 큐에 데이터 추가 (큐가 가득 차면 블로킹)
//                    queue.add(i); // 큐에 데이터 추가 (큐가 가득 차면 예외 발생)
//                    queue.offer(i); // 큐에 데이터 추가 (큐가 가득 차면 false 반환)
                    System.out.println("생산 완료: " + i);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    int number = queue.take(); // 큐에서 데이터 제거 (큐가 비어 있으면 블로킹)
//                    int number = queue.remove(); // 큐에서 데이터 제거 (큐가 비어 있으면 예외 발생)
//                    int number = queue.poll(); // 큐에서 데이터 제거 (큐가 비어 있으면 null 반환)
                    System.out.println("소비 완료: " + number);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
