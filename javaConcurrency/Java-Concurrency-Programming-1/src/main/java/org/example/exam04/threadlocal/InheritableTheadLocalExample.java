package org.example.exam04.threadlocal;

public class InheritableTheadLocalExample {
    public static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

//    public static ThreadLocal<String> inheritableThreadLocal = new ThreadLocal<>();
    public static void main(String[] args) {
        inheritableThreadLocal.set("부모 스레드 값");

        Thread childThread = new Thread(() -> {
            System.out.println("자식 스레드에서 부모 스레드로부터 상속 받은 값: " + inheritableThreadLocal.get());
            inheritableThreadLocal.set("자식 스레드의 새로운 값");
            System.out.println("자식 스레드에서 설정한 후의 값: " + inheritableThreadLocal.get());
        });

        childThread.start();

        try {
            childThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("부모 스레드에서 설정한 값: " + inheritableThreadLocal.get());
    }
}
