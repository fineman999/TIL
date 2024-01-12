package org.example.exam04.threadlocal;

public class ThreadLocalExample {

    private static final ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "기본값");
    public static void main(String[] args) {

        new Thread(() -> {
            System.out.printf("스레드 %s에서 설정한 값: %s\n", Thread.currentThread().getName(), threadLocal.get());
            threadLocal.set("스레드1에서 설정한 값");
            System.out.printf("스레드 %s에서 설정한 값: %s\n", Thread.currentThread().getName(), threadLocal.get());
            // 데이터가 삭제되면 기본값이 출력된다.
            threadLocal.remove();
            System.out.printf("스레드 %s에서 설정한 값: %s\n", Thread.currentThread().getName(), threadLocal.get());
        }, "Thread-1").start();

        new Thread(() -> {
            System.out.printf("스레드 %s에서 설정한 값: %s\n", Thread.currentThread().getName(), threadLocal.get());
            threadLocal.set("스레드2에서 설정한 값");
            System.out.printf("스레드 %s에서 설정한 값: %s\n", Thread.currentThread().getName(), threadLocal.get());
        }, "Thread-2").start();


    }
}
