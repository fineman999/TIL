package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.Item;
import io.chan.bookrentalservice.feature.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RentalCardTest {

    @DisplayName("RentCard를 생성한다")
    @Test
    void createRentalCard() {
        final IDName member = IDNameFixture.createIDName();


        final RentalCard rentalCard = RentalCard.createRentalCard(
                member
        );
        // then
        assertThat(rentalCard).isNotNull();
    }

    @DisplayName("대여를 하면 RentItem의 개수가 증가한다")
    @Test
    void rentItem() {
        final Item item = ItemFixture.createItem();

        RentalCard rentalCard = RentalCardFixture.createRentalCard();
        rentalCard.rentItem(item);

        assertThat(rentalCard.rentalItemCount()).isEqualTo(1);
    }

    @DisplayName("반납을 하면 RentItem의 개수가 감소한다")
    @Test
    void returnItem() {
        final Item item = ItemFixture.createItem();

        RentalCard rentalCard = RentalCardFixture.createRentalCard();
        rentalCard.rentItem(item);
        LocalDateTime now = LocalDateTime.now();
        rentalCard = rentalCard.returnItem(item, now);

        assertThat(rentalCard.rentalItemCount()).isZero();
    }
}