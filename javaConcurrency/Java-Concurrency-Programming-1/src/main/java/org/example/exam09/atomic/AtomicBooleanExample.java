package org.example.exam09.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanExample {

    private static final AtomicBoolean flag = new AtomicBoolean(false);
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                while (flag.compareAndSet(false, true)) {
                    System.out.println("flag가 false가 아닙니다.");
                    System.out.println("스레드 1이 대기중 입니다." + flag.get());
                }
                System.out.println("스레드 1이 flag를 true로 변경했습니다.");
                System.out.println("스레드 1이 작업을 수행합니다.");
                flag.set(false);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                while (flag.compareAndSet(false, true)) {
                    System.out.println("flag가 false가 아닙니다.");
                    System.out.println("스레드 2가 대기중 입니다." + flag.get());
                }
                System.out.println("스레드 2가 flag를 true로 변경했습니다.");
                System.out.println("스레드 2가 작업을 수행합니다.");
                flag.set(false);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
