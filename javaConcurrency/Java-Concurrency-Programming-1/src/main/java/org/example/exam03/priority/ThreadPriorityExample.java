package org.example.exam03.priority;

public class ThreadPriorityExample {
    // 제대로 된 결과가 나오지 않는다.
    public static void main(String[] args) throws InterruptedException {
        CountingThread 우선_순위가_높은_스레드 = new CountingThread("우선 순위가 높은 스레드", Thread.MAX_PRIORITY);
        CountingThread 우선_순위가_낮은_스레드 = new CountingThread("우선 순위가 낮은 스레드", Thread.MIN_PRIORITY);
        CountingThread 우선_순위가_기본인_스레드 = new CountingThread("우선 순위가 기본인 스레드", Thread.NORM_PRIORITY);

        우선_순위가_높은_스레드.start();
        우선_순위가_낮은_스레드.start();
        우선_순위가_기본인_스레드.start();

        우선_순위가_높은_스레드.join();
        우선_순위가_기본인_스레드.join();
        우선_순위가_낮은_스레드.join();

        System.out.println("main 스레드 종료");
    }

    static class CountingThread extends Thread {
        private final String threadName;
        private int count;

        public CountingThread(String threadName, int priority) {
            this.threadName = threadName;
            setPriority(priority);
        }

        @Override
        public void run() {
            while (count < 1000000) {
                count++;
            }
            System.out.println(threadName + "의 count 값 : " + count);
        }
    }
}
