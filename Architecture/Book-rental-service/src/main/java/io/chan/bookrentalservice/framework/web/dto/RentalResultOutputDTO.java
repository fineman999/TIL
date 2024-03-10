package io.chan.bookrentalservice.framework.web.dto;

import io.chan.bookrentalservice.domain.model.RentalCard;
import lombok.Builder;

@Builder
public record RentalResultOutputDTO(
    String userId,
    String username,
    Long rentedCount,
    Long totalLateFee
) {
    public static RentalResultOutputDTO from(final RentalCard rentalCard) {
        return RentalResultOutputDTO.builder()
                .userId(rentalCard.getMember().getId())
                .username(rentalCard.getMember().getName())
                .rentedCount(rentalCard.totalRentalCnt())
                .totalLateFee(rentalCard.getLateFee().getPoint())
                .build();
    }
}
