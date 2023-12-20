package org.example.exam03.sleep;

public class BasicSleepExample {
    public static void main(String[] args) {
        try {
            System.out.println("2초 후에 메시지가 출력됩니다.");
            Thread.sleep(2000);
            System.out.println("메시지가 출력되었습니다.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
