package io.chan.bookrentalservice.framework.web.dto;

import io.chan.bookrentalservice.domain.model.ReturnItem;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReturnItemOutputDTO(
        Long itemNo,
        String itemTitle,
        LocalDateTime returnDate
) {
    public static ReturnItemOutputDTO from(ReturnItem returnItem) {
        return ReturnItemOutputDTO.builder()
                .itemNo(returnItem.getRentalItem().getItem().getNo())
                .itemTitle(returnItem.getRentalItem().getItem().getTitle())
                .returnDate(returnItem.getReturnDateTime())
                .build();
    }
}
