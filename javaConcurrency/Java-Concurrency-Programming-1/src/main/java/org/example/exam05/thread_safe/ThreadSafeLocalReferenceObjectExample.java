package org.example.exam05.thread_safe;

public class ThreadSafeLocalReferenceObjectExample {
    class MyObject {
        private int value;

        public void increment() {
            value++;
        }

        @Override
        public String toString() {
            return "MyObject{" +
                    "value=" + value +
                    '}';
        }
    }

    public void printNumbers() {
        MyObject myObject = new MyObject();

        for (int i = 0; i < 10; i++) {
            myObject.increment();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(Thread.currentThread().getName() + ": " + myObject);
    }

    public static void main(String[] args) {
        ThreadSafeLocalReferenceObjectExample example = new ThreadSafeLocalReferenceObjectExample();

        Thread thread1 = new Thread(() -> {
            example.printNumbers();
        });

        Thread thread2 = new Thread(() -> {
            example.printNumbers();
        });

        thread1.start();
        thread2.start();
    }
}
