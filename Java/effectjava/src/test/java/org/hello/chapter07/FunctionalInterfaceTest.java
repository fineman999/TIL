package org.hello.chapter07;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalInterfaceTest {

    @Test
    @DisplayName("UnaryOperator<T>를 이용하여 문자열을 대문자로 변경한다.")
    void test() {
         UnaryOperator<String> toUpperCase = String::toUpperCase;
         assertThat(toUpperCase.apply("hello")).isEqualTo("HELLO");
    }

    @Test
    @DisplayName("UnaryOperator<T>를 이용하여 제곱하는 메서드를 만든다.")
    void unaryOperator() {
        UnaryOperator<Integer> square = x -> x * x;
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> changeIntegers = integers.stream().map(square).toList();

        assertThat(changeIntegers).containsExactly(1, 4, 9, 16, 25);
    }
}
