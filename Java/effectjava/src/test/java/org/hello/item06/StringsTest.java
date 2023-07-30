package org.hello.item06;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    @DisplayName("new String은 사용하지 말고 문자열 리터럴을 사용하라")
    void create() {
        String hello = "Hello";
        String hello2 = new String("Hello");
        String hello3 = "Hello";

        assertAll(
                () -> assertThat(hello).isEqualTo(hello2),
                () -> assertThat(hello).isNotSameAs(hello2),
                () -> assertThat(hello).isSameAs(hello3),
                () -> assertThat(hello).isEqualTo(hello3)
        );
    }
}