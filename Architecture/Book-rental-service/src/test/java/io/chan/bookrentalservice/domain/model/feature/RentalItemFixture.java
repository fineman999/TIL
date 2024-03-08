package io.chan.bookrentalservice.domain.model.feature;

import io.chan.bookrentalservice.domain.model.RentalItem;
import io.chan.bookrentalservice.domain.model.vo.Item;

import java.time.LocalDateTime;

public class RentalItemFixture {
    public static RentalItem createRentalItem() {
        final Item item = ItemFixture.createItem();
        final LocalDateTime now = LocalDateTime.parse("2021-07-01T00:00:00");// 현재 날짜 시간
        return RentalItem.createRentalItem(item, now);
    }
}
