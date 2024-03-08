package io.chan.bookrentalservice.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @DisplayName("책을 생성한다")
    @Test
    void createItem() {
        // given
        final Long no = 1L;
        final String title = "노인과 바다";

        // when
        final Item item = Item.createItem(no, title);

        // then
        assertThat(item.getNo()).isEqualTo(1L);
        assertThat(item.getTitle()).isEqualTo("노인과 바다");
    }
}