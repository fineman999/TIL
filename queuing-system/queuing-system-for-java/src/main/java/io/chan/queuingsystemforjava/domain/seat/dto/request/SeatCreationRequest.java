package io.chan.queuingsystemforjava.domain.seat.dto.request;

import java.util.List;

public record SeatCreationRequest (
        List<SeatCreationElement> seatCreationElements
){
}
