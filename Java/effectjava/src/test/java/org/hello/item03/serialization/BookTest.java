package org.hello.item03.serialization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BookTest {

    @Test
    @DisplayName("직렬화 테스트 - transient 키워드를 사용한 필드는 직렬화 되지 않는다.")
    void test() {
        Book book = new Book("12345", "이펙티브 자바 완벽 공략",
                "백기선", LocalDate.of(2020, 1, 1));
        book.setNumberOfSold(1000);

        SerializationExample example = new SerializationExample();
        example.serialize(book);
        Book deserializedBook = example.deserialize();

        assertAll(
                () -> assertThat(book.getNumberOfSold()).isEqualTo(1000),
                () -> assertThat(deserializedBook.getNumberOfSold()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("스태틱 클래스 변수 값이 변경 될 경우")
    void change() {
        Book book = new Book("12345", "이펙티브 자바 완벽 공략",
                "백기선", LocalDate.of(2020, 1, 1));
        book.setNumberOfSold(1000);
        Book.name = "keesun";

        SerializationExample example = new SerializationExample();
        example.serialize(book);
        Book.name = "whiteship";

        assertThat(Book.name).isEqualTo("whiteship");
    }

}