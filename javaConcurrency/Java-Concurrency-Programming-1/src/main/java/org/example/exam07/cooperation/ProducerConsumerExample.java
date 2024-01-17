package org.example.exam07.cooperation;

import java.util.LinkedList;
import java.util.Queue;

class SharedQueue {

    private Queue<Integer> queue = new LinkedList<>();
    private int CAPACITY = 5;

    private final Object lock = new Object();

    public void produce(int data) {
        synchronized (lock) {

            while (queue.size() == CAPACITY) {
                try {
                    System.out.println("생산 대기");
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            queue.add(data);
            System.out.println("생산: " + data);
            lock.notifyAll();
        }
    }

    public void consume() {
        synchronized (lock) {

            while (queue.isEmpty()) {
                try {
                    System.out.println("큐가 비었습니다. 소비 대기");
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            int data = queue.poll();
            System.out.println("소비: " + data);
            lock.notifyAll();
        }
    }
}

public class ProducerConsumerExample {
    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue();

        Thread 생산자 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sharedQueue.produce(i);
            }
        }, "생산자");

        Thread 소비자 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sharedQueue.consume();
            }
        }, "소비자");


        생산자.start();
        소비자.start();

        try {
            생산자.join();
            소비자.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
