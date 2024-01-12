package org.example.exam04.threadlocal.logger;

public class ThreadLocalLoggerExample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new LogWorker());
        Thread thread2 = new Thread(new LogWorker());
        Thread thread3 = new Thread(new LogWorker());

        thread1.start();
        thread2.start();
        thread3.start();


        thread1.join();
        thread2.join();
        thread3.join();
    }
}
