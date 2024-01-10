package org.example.exam04.threadgroup;

public class ThreadGroupExample {

    public static void main(String[] args) {
        ThreadGroup mainThreadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup customThreadGroup = new ThreadGroup("Custom Thread Group");

        // ThreadGroup을 지정하지 않으면 현재 스레드가 속한 스레드 그룹에 속하게 된다.
        Thread defaultGroupThread = new Thread(new GroupRunnable(), "DefaultGroupThread");

        // ThreadGroup을 지정하면 해당 스레드 그룹에 속하게 된다.
        Thread mainGroupThread = new Thread(mainThreadGroup, new GroupRunnable(), "MainGroupThread");

        Thread customGroupThread = new Thread(customThreadGroup, new GroupRunnable(), "CustomGroupThread");

        defaultGroupThread.start();
        mainGroupThread.start();
        customGroupThread.start();

    }

    static class GroupRunnable implements Runnable {
        @Override
        public void run() {
            Thread currentedThread = Thread.currentThread();
            System.out.printf("%s는 %s에 속한다.\n", currentedThread.getName(), currentedThread.getThreadGroup().getName());
        }
    }
}
