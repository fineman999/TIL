package io.chan.bookrentalservice.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class RentalCardNoTest {

    @DisplayName("RentCardNo을 생성한다")
    @Test
    void createRentalCardNo() {
        // given
        final UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        final LocalDateTime dateTime = LocalDateTime.parse("2024-01-01T00:00:00");

        // when
        final RentalCardNo rentalCardNo = RentalCardNo.createRentalCardNo(uuid, dateTime);

        // then
        assertThat(rentalCardNo).isNotNull();
        assertThat(rentalCardNo.getNo()).isEqualTo("2024-123e4567-e89b-12d3-a456-426614174000");
    }
}