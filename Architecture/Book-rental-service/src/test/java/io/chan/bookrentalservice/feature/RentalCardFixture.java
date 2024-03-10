package io.chan.bookrentalservice.feature;

import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.vo.IDName;

public class RentalCardFixture {
    public static RentalCard createRentalCard() {
        final IDName member = IDNameFixture.createIDName();
        return RentalCard.createRentalCard(
                member
        );
    }
}
