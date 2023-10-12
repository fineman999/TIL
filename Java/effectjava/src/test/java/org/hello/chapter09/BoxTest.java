package org.hello.chapter09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Comparator.naturalOrder;
import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    @Test
    @DisplayName("문자열 연결을 잘못 사용한 예 - 느리다!")
    void test() {
        String result = "";
        for (int i = 0; i < 100; i++) {
            result += i;
        }
    }
    @Test
    @DisplayName("StringBuilder를 사용하면 성능이 크게 개선된다.")
    void test2() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            result.append(i);
        }
    }

}