package org.hello.item08.cleaner_as_a_safetynet;

import java.lang.ref.Cleaner;

// cleaner를 안전망으로 활용한 AutoCloseable 클래스
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    public Room(int numJunkPiles) {
        this.state = new State(numJunkPiles);
        this.cleanable = cleaner.register(this, state);
    }

    // 청소가 필요한 자원, 절대 Room을 참조해서는 안 된다!
    private static class State implements Runnable {
        int numJunkPiles; // Room 안의 쓰레기 수
        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        // close 메서드나 cleaner를 호출하지 않고는 절대 호출되지 않는다.
        @Override
        public void run() {
            System.out.println("Cleaning room");
            numJunkPiles = 0;
        }
    }

    // 이 자원의 상태. cleanable과 공유한다.
    private final State state;

    // cleanable을 통해 state를 청소한다.
    private final Cleaner.Cleanable cleanable;

    @Override
    public void close() {
        cleanable.clean();
    }
}
