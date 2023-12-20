package org.example.exam03.join;

public class InterruptedJoinExample {
    public static void main(String[] args) {

        Thread mainThread = Thread.currentThread();

        Thread longRunningThread = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("긴 작업 스레드 실행 중 ...");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
//                mainThread.interrupt();
                System.out.println("긴 작업 스레드가 인터럽트되었습니다.");
            }
        });

        longRunningThread.start();

        Thread interruptinThread = new Thread(() -> {
            try {
                System.out.println("인터렙트 스레드가 5초 후에 긴 작업 스레드를 인터럽트합니다.");
                Thread.sleep(5000);
                longRunningThread.interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        interruptinThread.start();


        try {
            System.out.println("메인 스레드가 긴 작업 스레드의 종료를 기다립니다.");
            longRunningThread.join();
            System.out.println("메인 스레드가 계속 실행됩니다.");
        } catch (InterruptedException e) {
            System.out.println("메인 스레드가 인터럽트되었습니다.");
        }
    }
}
