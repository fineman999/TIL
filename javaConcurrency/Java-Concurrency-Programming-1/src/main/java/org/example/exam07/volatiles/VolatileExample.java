package org.example.exam07.volatiles;

public class VolatileExample {

    private volatile boolean running = true;

    private void volatileTest() {

        new Thread(() -> {
            int count = 0;
            // 만약 volatile 키워드가 없다면, 아래의 while 문은 무한 루프에 빠지게 된다.
            // 왜냐하면, running 변수의 값을 캐시에 저장하고, 캐시에 저장된 값을 읽어오기 때문이다.
            // 그러나 volatile 키워드가 있다면, 캐시에 저장된 값을 읽어오지 않고, 메인 메모리에 저장된 값을 읽어온다.
            // 따라서, running 변수의 값이 변경되면, 메인 메모리에 저장된 값을 읽어오기 때문에, while 문을 빠져나올 수 있다.
            while (running) {
                count++;
            }
            System.out.println("쓰레드 1 종료: " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            running = false;
            System.out.println("쓰레드 2 종료");
        }).start();
    }
    public static void main(String[] args) {
        new VolatileExample().volatileTest();
    }
}
