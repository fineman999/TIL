package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReturnItemTest {

    @DisplayName("아이템을 반납하면 지금 시점으로부터 반납일이 생성된다.")
    @Test
    void createReturnItem() {

        // given
        final RentalItem rentalItem = RentalItem.createRentalItem(
                Item.createItem(1L, "노인과 바다"),
                LocalDateTime.parse("2021-07-01T00:00:00")
        );

        // when
        final ReturnItem returnItem = ReturnItem.createReturnItem(
                rentalItem,
                LocalDateTime.parse("2021-07-15T00:00:00")
        );

        // then
        assertThat(returnItem.getReturnDateTime()).isEqualTo("2021-07-15T00:00:00");
    }
}