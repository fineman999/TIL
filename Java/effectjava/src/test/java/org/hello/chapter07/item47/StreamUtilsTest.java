package org.hello.chapter07.item47;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class StreamUtilsTest {

    @Test
    @DisplayName("StreamUtils.streamOf() 테스트: Iterable을 스트림으로 변환한다.")
    void test() {
        StreamUtils.streamOf(List.of(1, 2, 3)).forEach(System.out::println);
    }

    @Test
    @DisplayName("StreamUtils.iterableOf() 테스트: 스트림을 Iterable로 변환한다.")
    void test2() {
        StreamUtils.iterableOf(Stream.of(1, 2, 3)).forEach(System.out::println);
    }
}