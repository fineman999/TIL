package org.hello.chapter07.item47;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SubListsTest {

    @Test
    @DisplayName("SubLists.of() 테스트: 입력 리스트의 모든 부분리스트를 스트림으로 반환한다.")
    void test() {
        SubLists.of(List.of(1, 2, 3)).forEach(System.out::println);
    }

}