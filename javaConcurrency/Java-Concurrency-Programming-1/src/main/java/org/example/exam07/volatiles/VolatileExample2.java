package org.example.exam07.volatiles;

public class VolatileExample2 {

    private volatile int counter = 0;

    public void increment() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        VolatileExample2 volatileExample2 = new VolatileExample2();

        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                volatileExample2.increment();
            }
            System.out.println("쓰기 스레드가 종료되었습니다.");
        });


        Runnable reader = () -> {
            int localValue = -1;
            while (localValue < 1000) {
                localValue = volatileExample2.getCounter();
                System.out.println(Thread.currentThread().getName() + ": " + localValue);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("읽기 스레드가 종료되었습니다.");
        };

        // 하나의 쓰레드가 쓰기 작업을 하고, 10개의 쓰레드가 읽기 작업을 한다.
        // 쓰기 작업은 즉각적으로 메인 메모리에 반영되고, 읽기 작업은 메인 메모리에 저장된 값을 읽어온다.
        thread.start();
        for(int i = 0; i < 10; i++) {
            new Thread(reader).start();
        }
    }

}
