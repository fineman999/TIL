package org.hello.chapter08.item55;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class MathUtilsTest {

    @Test
    @DisplayName("옵셔널 활용 1 - 기본값을 정해둘 수 있다.")
    void test() {
        List<Integer> numbers = List.of(1, 9, 3, 4, 5);
        MathUtils.maxOptional(numbers).orElse(0);
    }

    @Test
    @DisplayName("옵셔널 활용 2 - 원하는 예외를 던질 수 있다.")
    void test2() {
        List<Integer> numbers = List.of(1, 9, 3, 4, 5);
        MathUtils.maxOptional(numbers).orElseThrow(IllegalArgumentException::new);
    }

    @Test
    @DisplayName("옵셔널 활용 3 - 항상 값이 채워져 있다고 가정한다.")
    void test3() {
        List<Integer> numbers = List.of(1, 9, 3, 4, 5);
        MathUtils.maxOptional(numbers).get();
    }
}