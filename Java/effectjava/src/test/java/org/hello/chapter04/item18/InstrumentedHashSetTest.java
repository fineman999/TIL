package org.hello.chapter04.item18;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstrumentedHashSetTest {

    @Test
    @DisplayName("상속을 잘못 사용했을 때")
    void test() {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(java.util.List.of("틱", "탁탁", "펑"));
        System.out.println(s.getAddCount());
    }

    @Test
    @DisplayName("상속을 잘 사용했을 때")
    void test2() {
        InstrumentedSet<String> s = new InstrumentedSet<>(new java.util.HashSet<>());
        s.addAll(java.util.List.of("틱", "탁탁", "펑"));
        System.out.println(s.getAddCount());
    }
}