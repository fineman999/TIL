package org.example.exam04.threadgroup;

public class NestedThreadGroupExample {
    public static void main(String[] args) throws InterruptedException {


        ThreadGroup topGroup = new ThreadGroup("최상위 스레드 그룹");

        ThreadGroup subGroup = new ThreadGroup(topGroup, "하위 스레드 그룹 1");

        Thread topGroupThread = new Thread(topGroup, new GroupRunnable(), "topGroupThread");
        Thread subGroupThread = new Thread(subGroup, new GroupRunnable(), "subGroupThread");

        topGroupThread.start();
        subGroupThread.start();

        Thread.sleep(1000);

        System.out.printf("-- 최상위 스레드 그룹의 정보 --\n");
        topGroup.list();
    }

    static class GroupRunnable implements Runnable {
        @Override
        public void run() {
            Thread currentedThread = Thread.currentThread();
            System.out.printf("%s는 %s에 속한다.\n", currentedThread.getName(), currentedThread.getThreadGroup().getName());
        }
    }
}
