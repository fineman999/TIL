package org.example.exam03.sleep;

public class InterruptSleepExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("20초 동안 잠듭니다. 인터렙트되지 않는다면 계속 잠들어 있을 것입니다.");
                Thread.sleep(20000);
                System.out.println("인터렙트 없이 잠에서 깨어났습니다.");
            } catch (InterruptedException e) {
                System.out.printf("인터렙트를 받았습니다. 스레드 상태: %s\n", Thread.currentThread().getState());
            }
        });

        thread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread.interrupt();
    }
}
