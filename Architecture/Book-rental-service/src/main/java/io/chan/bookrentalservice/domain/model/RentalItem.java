package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalItem {
    private static final int RETURN_DAYS = 14;
    private Item item;
    private LocalDateTime rentDateTime;
    private LocalDateTime returnDateTime;
    private boolean overdue;


    public static RentalItem createRentalItem(
            Item item,
            LocalDateTime now
    ) {
        final LocalDateTime returnDateTime = now.plusDays(RETURN_DAYS); // 반납 날짜 - 대여 날짜 + 14일
        boolean overdue = false;
        return new RentalItem(item, now, returnDateTime, overdue);
    }

    public boolean equalItem(final Item item) {
        return this.item.equals(item);
    }

    public int calculateOverDueDays(final LocalDateTime returnDate) {
        if (this.compareToDate(returnDate)) {
            return Period.between(this.returnDateTime.toLocalDate(), returnDate.toLocalDate()).getDays();
        }
        return 0;
    }

    private boolean compareToDate(final LocalDateTime returnDate) {
        return returnDateTime.isBefore(returnDate);
    }

    public void setOverdue(final boolean valid) {
        this.overdue = valid;
    }
}
