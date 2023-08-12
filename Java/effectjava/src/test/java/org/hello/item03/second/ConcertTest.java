package org.hello.item03.second;

import org.hello.chapter01.item03.second.Concert;
import org.hello.chapter01.item03.second.Elvis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConcertTest {


    @Test
    @DisplayName("정적 팩터리의 메서드 참조를 공급자(Supplier)로 사용할 수 있다")
    void test() {
        Concert concert = new Concert();
        concert.start(Elvis::getInstance);
    }
}