package org.example.exam04.threadgroup;

public class ThreadGroupScopeExample {
    public static void main(String[] args) throws InterruptedException {
        ThreadGroup topGroup = new ThreadGroup("최상위 스레드 그룹");

        ThreadGroup subGroup = new ThreadGroup(topGroup, "최하위 스레드 그룹");

        Thread topGroupThread = new Thread(topGroup, () -> {
            System.out.printf("상위 그룹 스레드에서 하위 그룹의 최대 우선 순위 설정 변경 전: %d\n", subGroup.getMaxPriority());
            subGroup.setMaxPriority(7);
            System.out.printf("상위 그룹 스레드에서 하위 그룹의 최대 우선 순위 설정 변경 후: %d\n", subGroup.getMaxPriority());
        }, "상위 스레드 그룹");


        Thread subGroupThread = new Thread(subGroup, () -> {
            System.out.printf("하위 그룹 스레드에서 상위 그룹의 최대 우선 순위 설정 변경 전: %d\n", subGroup.getMaxPriority());
            topGroup.setMaxPriority(6);
            System.out.printf("하위 그룹 스레드에서 상위 그룹의 최대 우선 순위 설정 변경 후: %d\n", subGroup.getMaxPriority());
        }, "하위 스레드 그룹");

        topGroupThread.start();
        subGroupThread.start();


        topGroupThread.join();
        subGroupThread.join();

        System.out.printf("%s: %d\n", topGroup.getName(), topGroup.getMaxPriority());
        System.out.printf("%s: %d\n", subGroup.getName(), subGroup.getMaxPriority());

        Thread userThread1 = new Thread(topGroup, () -> {
        }, "유저 스레드 1");

        Thread userThread2 = new Thread(subGroup, () -> {
        }, "유저 스레드 2");

        userThread1.start();
        userThread2.start();

        userThread1.join();
        userThread2.join();

        System.out.printf("%s: %d\n", userThread1.getName(), userThread1.getPriority());
        System.out.printf("%s: %d\n", userThread2.getName(), userThread2.getPriority());

    }
}
