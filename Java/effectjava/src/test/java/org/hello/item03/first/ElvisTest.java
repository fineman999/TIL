package org.hello.item03.first;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ElvisTest {

    @Test
    @DisplayName("Elvis 클래스의 인스턴스가 하나만 생성되는지 테스트한다.")
    void create() {
        Elvis elvis = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;

        assertAll(
                () -> assertThat(elvis).isEqualTo(elvis2),
                () -> assertThat(elvis).isSameAs(elvis2)
        );
    }
}