package io.chan.bookrentalservice.domain.model.vo;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RentalCardNo {
    private final String no;

    public static RentalCardNo createRentalCardNo(
            UUID uuid,
            LocalDateTime dateTime
    ) {
        final int year = dateTime.getYear();
        final String format = String.format("%d-%s", year, uuid);
        return new RentalCardNo(format);
    }
}
