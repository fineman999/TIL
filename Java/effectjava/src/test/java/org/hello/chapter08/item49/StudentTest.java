package org.hello.chapter08.item49;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class StudentTest {

    @Test
    @DisplayName("requireNonNull")
    void requireNonNull() {
        Student student = new Student();

        Objects.requireNonNull(student, "student is null");

        Objects.checkFromIndexSize(1, 2, 3);
    }


    @Test
    @DisplayName("assert test")
    void assertTest() {


        Assertions.assertThatThrownBy(
                () -> Student.sort(null, 1, 2)
        ).isInstanceOf(AssertionError.class);
    }
}