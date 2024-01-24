package org.example.exam08.condition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerProducerExample {
    private static final int CAPACITY = 5;
    private Queue<Integer> queue = new LinkedList<>();
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public void produce(int data) {
        while (true) {
            lock.lock();
            try {
                while (queue.size() == CAPACITY) {
                    System.out.println("큐가 가득 차서 기다립니다.");
                    notFull.await();
                }
                queue.offer(data);
                System.out.println("큐에 데이터를 하나 넣었습니다. 큐 크기 : " + queue.size() + ", 데이터 : " + data);
                notEmpty.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public void consume() {
        while (true) {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    System.out.println("큐가 비어서 기다립니다.");
                    notEmpty.await();
                }
                Integer data = queue.poll();
                System.out.println("큐에서 데이터를 하나 꺼냈습니다. 큐 크기 : " + queue.size() + ", 데이터 : " + data);
                notFull.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {

        ConsumerProducerExample consumerProducerExample = new ConsumerProducerExample();

        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                consumerProducerExample.produce(i);
            }
        });

        Thread consumerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                consumerProducerExample.consume();
            }
        });

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
