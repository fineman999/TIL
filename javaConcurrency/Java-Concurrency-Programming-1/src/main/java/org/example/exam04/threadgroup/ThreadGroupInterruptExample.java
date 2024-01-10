package org.example.exam04.threadgroup;

public class ThreadGroupInterruptExample {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup topGroup = new ThreadGroup("상위그룹");
        ThreadGroup subGroup = new ThreadGroup(topGroup, "하위그룹");

        Thread topGroupThread = new Thread(topGroup, () -> {
            while (true) {
                System.out.printf("상위그룹 스레드가 실행 중입니다.\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("상위그룹 스레드가 인터럽트 되었습니다.");
                    break;
                }
            }
        }, "상위그룹 스레드");

        Thread subGroupThread = new Thread(subGroup, () -> {
            while (true) {
                System.out.printf("하위그룹 스레드가 실행 중입니다.\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("하위그룹 스레드가 인터럽트 되었습니다.");
                    break;
                }
            }
        }, "하위그룹 스레드");

        topGroupThread.start();
        subGroupThread.start();

        Thread.sleep(3000);

        System.out.println("그룹 스레드를 중지..");

//        topGroup.interrupt();
        subGroup.interrupt();

    }
}
