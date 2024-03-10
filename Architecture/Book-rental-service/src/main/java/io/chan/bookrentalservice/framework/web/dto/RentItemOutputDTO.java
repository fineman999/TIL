package io.chan.bookrentalservice.framework.web.dto;

import io.chan.bookrentalservice.domain.model.RentalItem;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentItemOutputDTO(
        Long itemNo,
        String itemTitle,
        LocalDateTime rentDate,
        boolean overdue,
        LocalDateTime returnDate
) {
    public static RentItemOutputDTO from(RentalItem rentalItem) {
        return RentItemOutputDTO.builder()
                .itemNo(rentalItem.getItem().getNo())
                .itemTitle(rentalItem.getItem().getTitle())
                .rentDate(rentalItem.getRentDateTime())
                .overdue(rentalItem.isOverdue())
                .returnDate(rentalItem.getExpireDateTime())
                .build();
    }
}
