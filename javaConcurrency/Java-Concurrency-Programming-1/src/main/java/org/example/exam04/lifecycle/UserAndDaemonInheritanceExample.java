package org.example.exam04.lifecycle;

public class UserAndDaemonInheritanceExample {
    public static void main(String[] args) throws InterruptedException {
        Thread userThread = new Thread(() -> {
            new Thread(() -> {
                // 사용자 스레드의 자식 스레드는 사용자 스레드가 된다. (상속)
                System.out.println("사용자 스레드의 자식 스레드의 데몬 여부: " + Thread.currentThread().isDaemon());
            }).start();
            System.out.println("사용자 스레드의 데몬 여부: " + Thread.currentThread().isDaemon());
        });

        Thread daemonThread = new Thread(() -> {
            // 데몬 스레드의 자식 스레드는 데몬 스레드가 된다. (상속)
            new Thread(() -> {
                System.out.println("데몬 스레드의 자식 스레드의 데몬 여부: " + Thread.currentThread().isDaemon());
            }).start();
            System.out.println("데몬 스레드의 데몬 여부: " + Thread.currentThread().isDaemon());
        });

        // 데몬 스레드로 설정한다.
        daemonThread.setDaemon(true);

        userThread.start();
        daemonThread.start();

        userThread.join();

    }
}
