package io.chan.bookrentalservice.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ReturnItem {
    @Embedded
    private RentalItem rentalItem;
    private LocalDateTime returnDateTime;

    public static ReturnItem createReturnItem(
            RentalItem rentalItem,
            LocalDateTime now
    ) {
        return new ReturnItem(rentalItem, now);
    }
}
