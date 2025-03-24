package io.chan.queuingsystemforjava.domain.seat.dto.request;

import jakarta.validation.Valid;

import java.util.List;

public record SeatGradeCreationRequest(
        @Valid List<SeatGradeCreationElement> seatGradeCreationElements
) {
}
