package io.chan.bookrentalservice.feature;

import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.Item;

import java.time.LocalDateTime;

public class RentalCardFixture {
    public static RentalCard createRentalCard() {
        final IDName member = IDNameFixture.createIDName();
        return RentalCard.createRentalCard(
                member
        );
    }

    public static void rent(final RentalCard card, final Item item) {
        card.rentItem(item);

    }

    public static void returnItem(final RentalCard rentalCard, final Item item) {
        rentalCard.returnItem(item, LocalDateTime.now());
    }

    public static void overdueItem(final RentalCard rentalCard, final Item item2) {
        rentalCard.overdueItem(item2);
    }

    public static void returnItemAfter16Days(final RentalCard rentalCard, final Item item2) {
        rentalCard.returnItem(item2, LocalDateTime.now().plusDays(16));
    }

    public static void makeAvailable(final RentalCard rentalCard, Long point) {
        rentalCard.makeAvailableRental(point);
    }
}
