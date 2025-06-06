package io.chan.queuingsystemforjava.domain.seat.dto.response;

import io.chan.queuingsystemforjava.domain.seat.SeatGrade;

import java.math.BigDecimal;

public record SeatGradeElement (
        Long seatGradeId,
        String gradeName,
        BigDecimal price
){
    public static SeatGradeElement of(SeatGrade seatGrade) {
        return new SeatGradeElement(seatGrade.getSeatGradeId(), seatGrade.getGradeName(), seatGrade.getPrice());
    }
}
