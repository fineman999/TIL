package io.chan.queuingsystemforjava.domain.seat.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SeatGradeCreationElement(
        @NotNull(message = "가격 필드는 비어있을 수 없습니다.")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        BigDecimal price,
        @NotNull(message = "좌석 등급 이름은 비어있을 수 없습니다.")
        @Size(min = 1, max = 20, message = "좌석 등급 이름은 1자 이상 20자 이하여야 합니다.")
        String seatGradeName
) {
}
