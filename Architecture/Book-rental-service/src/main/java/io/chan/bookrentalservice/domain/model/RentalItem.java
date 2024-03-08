package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalItem {
    private Item item;
    private LocalDateTime rentDateTime;
    private LocalDateTime returnDateTime;
    private boolean overdue;


    public static RentalItem createRentalItem(
            Item item,
            LocalDateTime now
    ) {
        final LocalDateTime returnDateTime = now.plusDays(14); // 반납 날짜 - 대여 날짜 + 14일
        boolean overdue = false;
        return new RentalItem(item, now, returnDateTime, overdue);
    }
}
