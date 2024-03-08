package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RentalItemTest {

    @DisplayName("책을 대여하면 지금 시점으로부터 14 일 후에 반납일이 생성된다.")
    @Test
    void createRentalItem() {

        // given
        final LocalDateTime now = LocalDateTime.parse("2021-07-01T00:00:00"); // 현재 날짜 시간
        final Item item = Item.createItem(1L, "노인과 바다");

        // when
        final RentalItem rentalItem = RentalItem.createRentalItem(item, now);

        // then
        assertThat(rentalItem.getRentDateTime()).isEqualTo("2021-07-01T00:00:00");
        assertThat(rentalItem.getReturnDateTime()).isEqualTo("2021-07-15T00:00:00");
    }
}