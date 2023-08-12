package org.hello.item08.cleaner_as_a_safetynet;

import org.hello.chapter01.item08.cleaner_as_a_safetynet.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    @DisplayName("Cleaner 안전망을 갖춘 자원을 제대로 활용하지 못하는 클라이언트")
    void teenager() {

        new Room(99);
        System.out.println("Peace out");

        // Uncomment this line to see that the cleaner is called:
        // 단, 가비지 컬렉터를 강제로 호출하는 이런 방식에 의존해서는 절대 안 된다.
         System.gc();
    }

    @Test
    @DisplayName("Cleaner 안전망을 갖춘 자원을 제대로 활용하는 클라이언트")
    void adult() {
        try (Room myRoom = new Room(7)) {
            System.out.println("안녕~");
        }
    }
}