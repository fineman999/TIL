package io.chan.bookrentalservice.feature;

import io.chan.bookrentalservice.domain.model.vo.RentalCardNo;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentalCardNoFixture {
    public static RentalCardNo createRentalCardNo() {
        final UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final LocalDateTime dateTime = LocalDateTime.parse("2024-01-01T00:00:00");
        return RentalCardNo.createRentalCardNo(uuid, dateTime);
    }
}
