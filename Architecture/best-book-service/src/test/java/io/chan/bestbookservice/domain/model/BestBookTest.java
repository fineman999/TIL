package io.chan.bestbookservice.domain.model;

import io.chan.bestbookservice.domain.vo.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BestBookTest {

    @DisplayName("book 생성")
    @Test
    void register() {
        // given
        Item item = Item.of(1L, "test");
        // when
        BestBook bestBook = BestBook.register(item, UUID.randomUUID());
        // then
        assertThat(bestBook).isNotNull();
    }

    @DisplayName("대여횟수 증가")
    @Test
    void increaseRentCount() {
        // given
        Item item = Item.of(1L, "test");
        BestBook bestBook = BestBook.register(item, UUID.randomUUID());
        // when
        Long rentCount = bestBook.increaseRentCount();
        // then
        assertThat(rentCount).isEqualTo(2);
    }
}