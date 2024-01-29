package org.example.exam09.atomic_field_updater;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterCASExample {
    private static AtomicIntegerFieldUpdater<MyClass> filedUpdater = AtomicIntegerFieldUpdater.newUpdater(MyClass.class, "value");
    public static class MyClass {
        private volatile int value;
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "value=" + value +
                    '}';
        }
    }
    private static final int NUM_THREADS = 5;
    public static void main(String[] args) {
        MyClass myClass = new MyClass();

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    int expectedValue;
                    int newValue;
                    do {
                        expectedValue = filedUpdater.get(myClass);
                        newValue = expectedValue + 1;
                    } while (!filedUpdater.compareAndSet(myClass, expectedValue, newValue));
//                    System.out.println("스레드 " + Thread.currentThread().getName() + "가 " + expectedValue + "에서 " + newValue + "로 변경했습니다.");
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("최종 결과: " + filedUpdater.get(myClass));
    }
}
