package org.hello.chapter02.item11.hashcode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    @Test
    @DisplayName("hashCode가 없을 경우")
    void test() {
        Map<PhoneNumber, String> map = new HashMap<>();
        PhoneNumber number1 = new PhoneNumber(111, 222, 3333);
        PhoneNumber number2 = new PhoneNumber(111, 222, 3333);

        assertThat(number1.equals(number2)).isTrue();
        System.out.println(number1.hashCode());
        System.out.println(number2.hashCode());

        map.put(number1, "hello");
        map.put(number2, "world");

        System.out.println(map.get(new PhoneNumber(111, 222, 3333)));

    }
}