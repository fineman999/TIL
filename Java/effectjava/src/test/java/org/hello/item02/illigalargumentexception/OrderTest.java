package org.hello.item02.illigalargumentexception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class OrderTest {

    @Test
    @DisplayName("배송일이 null일 경우 예외를 던진다.")
    void nullPointException() {
        Order order = new Order();
        Assertions.assertThatThrownBy(() -> order.updateDeliveryDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("배송일은 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("배송일이 오늘 이전일 경우 예외를 던진다.")
    void illegalArgumentException() {
        Order order = new Order();
        Assertions.assertThatThrownBy(() -> order.updateDeliveryDate(LocalDate.of(2020, 12, 8)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("배송일은 오늘 " + LocalDate.now() + "이후로 설정해주세요.");
    }
}