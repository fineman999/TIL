package io.chan.bookrentalservice.domain.model.feature;

import io.chan.bookrentalservice.domain.model.RentalItem;
import io.chan.bookrentalservice.domain.model.ReturnItem;

import java.time.LocalDateTime;

public class ReturnItemFixture {
    public static ReturnItem createReturnItem() {
        final RentalItem rentalItem = RentalItemFixture.createRentalItem();
        final LocalDateTime now = LocalDateTime.parse("2021-10-01T00:00:00");// 현재 날짜 시간
        return ReturnItem.createReturnItem(rentalItem, now);
    }
}
