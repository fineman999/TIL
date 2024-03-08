package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.LateFee;
import io.chan.bookrentalservice.domain.model.vo.RentStatus;
import io.chan.bookrentalservice.domain.model.vo.RentalCardNo;
import io.chan.bookrentalservice.feature.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RentalCardTest {

    @DisplayName("RentCard를 생성한다")
    @Test
    void createRentalCard() {
        // given
        final RentalCardNo rentalCardNo = RentalCardNoFixture.createRentalCardNo();
        final IDName member = IDNameFixture.createIDName();
        final RentStatus rentAvailable = RentStatus.RENT_AVAILABLE;
        final LateFee lateFee = LateFeeFixture.createLateFee();


        final RentalCard rentalCard = RentalCard.createRentalCard(
                rentalCardNo,
                member,
                rentAvailable,
                lateFee
        );
        // then
        assertThat(rentalCard).isNotNull();
    }

    @DisplayName("RentItem을 추가하면, RentItem의 개수가 증가한다")
    @Test
    void addRentItem() {
        // given
        final RentalCard rentalCard = RentalCardFixture.createRentalCard();
        final RentalItem rentalItem = RentalItemFixture.createRentalItem();
        rentalCard.addRentItem(rentalItem);
        assertThat(rentalCard.rentalItemCount()).isEqualTo(1);
    }

    @DisplayName("RentItem을 삭제하면, RentItem의 개수가 감소한다")
    @Test
    void removeRentItem() {
        // given
        final RentalCard rentalCard = RentalCardFixture.createRentalCard();
        final RentalItem rentalItem = RentalItemFixture.createRentalItem();
        rentalCard.addRentItem(rentalItem);
        rentalCard.removeRentItem(rentalItem);
        assertThat(rentalCard.rentalItemCount()).isZero();
    }

@DisplayName("ReturnItem을 추가하면, ReturnItem의 개수가 증가한다")
    @Test
    void addReturnItem() {
        // given
        final RentalCard rentalCard = RentalCardFixture.createRentalCard();
        final ReturnItem returnItem = ReturnItemFixture.createReturnItem();
        rentalCard.addReturnItem(returnItem);
        assertThat(rentalCard.returnItemCount()).isEqualTo(1);
    }

    @DisplayName("ReturnItem을 삭제하면, ReturnItem의 개수가 감소한다")
    @Test
    void removeReturnItem() {
        // given
        final RentalCard rentalCard = RentalCardFixture.createRentalCard();
        final ReturnItem returnItem = ReturnItemFixture.createReturnItem();
        rentalCard.addReturnItem(returnItem);
        rentalCard.removeReturnItem(returnItem);
        assertThat(rentalCard.returnItemCount()).isZero();
    }
}