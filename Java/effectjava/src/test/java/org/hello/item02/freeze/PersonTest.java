package org.hello.item02.freeze;

import org.hello.chapter01.item02.freeze.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    @Test
    @DisplayName("사람을 생성한다.")
    void create() {
        Person person = new Person("홍길동", 20);
        assertNotNull(person);
    }
}