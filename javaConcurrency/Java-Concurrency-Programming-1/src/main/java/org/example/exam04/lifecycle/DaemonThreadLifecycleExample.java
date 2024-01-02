package org.example.exam04.lifecycle;

public class DaemonThreadLifecycleExample {
    public static void main(String[] args) throws InterruptedException {
        Thread userThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("사용자 스레드 실행 중");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("데몬 스레드 실행 중");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // 데몬 스레드로 설정한다.
        daemonThread.setDaemon(true);

        userThread.start();
        daemonThread.start();

        userThread.join();
        System.out.println("메인 스레드 종료");

    }
}
