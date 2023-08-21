package org.hello.chapter07.item42;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class LambdaTest {

    @Test
    @DisplayName("익명 클래스의 인스턴스를 함수 객체로 사용 - 낡은 기법이다!")
    void name() {
        List<String> words = new java.util.ArrayList<>(List.of("abc", "abcd", "abcde", "abcdef", "abcdefg"));
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        });
    }

    @Test
    @DisplayName("람다식을 함수 객체로 사용 - 익명 클래스보다 간결하고 가독성이 좋다.")
    void name2() {
        List<String> words = new java.util.ArrayList<>(List.of("abc", "abcd", "abcde", "abcdef", "abcdefg"));
        Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
    }

    @Test
    @DisplayName("메서드 참조를 함수 객체로 사용 - 람다보다 간결하다.(비교자 생성 메서드)")
    void name3() {
        List<String> words = new java.util.ArrayList<>(List.of("abc", "abcd", "abcde", "abcdef", "abcdefg"));
        Collections.sort(words, Comparator.comparingInt(String::length));
    }

    @Test
    @DisplayName("List 인터페이스의 sort 메서드를 사용")
    void name4() {
        List<String> words = new java.util.ArrayList<>(List.of("abc", "abcd", "abcde", "abcdef", "abcdefg"));
        words.sort(Comparator.comparingInt(String::length));
    }
}