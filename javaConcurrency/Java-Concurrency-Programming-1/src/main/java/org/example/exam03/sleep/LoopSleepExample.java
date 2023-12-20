package org.example.exam03.sleep;

public class LoopSleepExample {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("반복: " + i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
