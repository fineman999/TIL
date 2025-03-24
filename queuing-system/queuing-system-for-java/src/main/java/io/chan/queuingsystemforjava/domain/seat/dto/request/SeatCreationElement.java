package io.chan.queuingsystemforjava.domain.seat.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatCreationElement(
        @NotBlank(message = "좌석 코드는 필수입니다.")
        String seatCode,
        @NotNull(message = "좌석 등급을 선택해주세요.")
        @Min(value = 1L, message = "좌석 등급을 선택해주세요.")
        Long seatGradeId
) {
}
