package org.example.exam08.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionExample {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean flag = false;

    public void awaiting() {
        lock.lock();
        try {
            while (!flag) {
                System.out.println("조건이 만족되지 않아 대기합니다.");
                condition.await();
            }
            System.out.println("임계 영역을 추가적으로 처리합니다.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void signaling() {
        lock.lock();
        try {
            System.out.println("조건을 만족했으니 signal을 보냅니다.");
            flag = true;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) {

        ConditionExample conditionExample = new ConditionExample();

        Thread thread1 = new Thread(() -> {
            conditionExample.awaiting();
            System.out.println("조건이 만족되어 스레드가 깨어났습니다.");
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                conditionExample.signaling();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
