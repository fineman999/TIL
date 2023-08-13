package org.hello.chapter04.item17;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PersonTest {

    @Test
    @DisplayName("방어적 복사를 통한 불변 객체로 만들기")
    void test() {
        Address seoul = new Address("12345", "강남대로", "서울시");
        Person person = new Person(seoul);

        Address busan = person.getAddress();
        busan.setCity("부산시");
        assertThat(seoul.getCity()).isEqualTo("서울시");
        assertThat(busan.getCity()).isEqualTo("부산시");
    }
}