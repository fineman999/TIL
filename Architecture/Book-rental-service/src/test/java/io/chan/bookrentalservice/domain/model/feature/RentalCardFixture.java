package io.chan.bookrentalservice.domain.model.feature;

import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.LateFee;
import io.chan.bookrentalservice.domain.model.vo.RentStatus;
import io.chan.bookrentalservice.domain.model.vo.RentalCardNo;

public class RentalCardFixture {
    public static RentalCard createRentalCard() {
        final RentalCardNo rentalCardNo = RentalCardNoFixture.createRentalCardNo();
        final IDName member = IDNameFixture.createIDName();
        final RentStatus rentAvailable = RentStatus.RENT_AVAILABLE;
        final LateFee lateFee = LateFeeFixture.createLateFee();

        return RentalCard.createRentalCard(
                rentalCardNo,
                member,
                rentAvailable,
                lateFee
        );
    }
}
