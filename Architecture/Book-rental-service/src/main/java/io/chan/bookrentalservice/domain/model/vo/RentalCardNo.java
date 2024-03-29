package io.chan.bookrentalservice.domain.model.vo;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RentalCardNo implements Serializable {
    @Serial
    private static final long serialVersionUID = 5735022999922182249L;
    private String no;
    public static RentalCardNo createRentalCardNo(
            UUID uuid,
            LocalDateTime dateTime
    ) {
        final int year = dateTime.getYear();
        final String format = String.format("%d-%s", year, uuid);
        return new RentalCardNo(format);
    }
}
