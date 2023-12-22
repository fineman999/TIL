package org.example.exam04;

public class FlagThreadStopExample {

    // volatile 키워드를 사용하면 스레드가 직접 변수에 접근하도록 강제한다.
    // volatile 키워드를 사용하면 스레드가 변수의 값을 읽을 때 캐시를 사용하지 않고 메인 메모리에서 읽어온다.
//    private volatile static boolean running = true;

    // volatile 키워드를 사용하지 않으면 스레드가 변수의 값을 캐시에 저장하고 읽어온다.
    // 따라서 스레드가 변수의 값을 읽을 때 메인 메모리에서 읽어오지 않고 캐시에서 읽어오기 때문에
    // 스레드가 변수의 값을 읽지 못하고 무한 루프에 빠지게 된다.
    private static boolean running = true;

    public static void main(String[] args) {
        new Thread(() -> {
            int count = 0;
            while (running) {
                // 컨텍스트 스위칭을 이용한 스레드 간 변수 값 동기화
                // Thread.sleep() 메소드를 사용하면 스레드가 일시 정지 상태가 되면서
                // 컨텍스트 스위칭이 발생한다.
                // 컨텍스트 스위칭이 발생하면 스레드의 작업 내용을 저장하고 다른 스레드의 작업 내용을 읽어온다.(TCB: 문맥정보 교환)
                // 이때 캐시에 저장된 변수의 값을 모두 비워버리기 때문에
                // 스레드가 변수의 값을 읽을 때 메인 메모리에서 읽어오게 된다.
                // 따라서 스레드가 변수의 값을 Thread 2에서 false로 변경하면
                // 문맥정보에 없기 떄문에 스레드 1에서는 메인 메모리에서 변수의 값을 읽어오게 된다.
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count ++;
            }
            System.out.println("Thread 1 종료, count : " + count);
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread 2 종료");
            running = false;
        }).start();


    }
}
