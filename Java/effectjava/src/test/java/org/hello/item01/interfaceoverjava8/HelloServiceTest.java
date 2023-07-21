package org.hello.item01.interfaceoverjava8;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloServiceTest {

    @Test
    @DisplayName("내림차순으로 정렬하는 Comparator를 만들고 List<Integer>를 정렬")
    void test() {

        Comparator<Integer> desc = (o1, o2) -> o2 - o1;
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(100);
        numbers.add(20);
        numbers.add(30);
        numbers.add(4);

        numbers.sort(desc);

        assertAll(
                () -> assertEquals(100, numbers.get(0)),
                () -> assertEquals(30, numbers.get(1)),
                () -> assertEquals(20, numbers.get(2)),
                () -> assertEquals(4, numbers.get(3))
        );
    }
    @Test
    @DisplayName(" 질문 1에서 만든 Comparator를 사용해서 오름차순으로 정렬")
    void testAsc() {

        Comparator<Integer> desc = (o1, o2) -> o2 - o1;
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(100);
        numbers.add(20);
        numbers.add(30);
        numbers.add(4);

        numbers.sort(desc.reversed());

        assertAll(
                () -> assertEquals(4, numbers.get(0)),
                () -> assertEquals(20, numbers.get(1)),
                () -> assertEquals(30, numbers.get(2)),
                () -> assertEquals(100, numbers.get(3))
        );
    }
}