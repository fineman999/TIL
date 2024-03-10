package io.chan.bookrentalservice.framework.web.dto;

import io.chan.bookrentalservice.domain.model.RentalCard;
import lombok.Builder;
import org.springframework.util.Assert;

@Builder
public record RentalCardOutputDTO(
        String rentalCardId,
        String memberId,
        String memberName,
        //대여가능여부
        String rentStatus,
        //전체연체료
        Long totalLateFee,
        //전체대여도서건수
        Long totalRentalCnt,
        //반납도서건수
        Long totalReturnCnt,
        //연체중인도서건수
        Long totalOverdueCnt
) {
    public RentalCardOutputDTO {
        Assert.hasText(rentalCardId, "rentalCardId must not be empty");
        Assert.hasText(memberId, "memberId must not be empty");
        Assert.hasText(memberName, "memberName must not be empty");
        Assert.hasText(rentStatus, "rentStatus must not be empty");
        Assert.notNull(totalLateFee, "totalLateFee must not be null");
        Assert.notNull(totalRentalCnt, "totalRentalCnt must not be null");
        Assert.notNull(totalReturnCnt, "totalReturnCnt must not be null");
        Assert.notNull(totalOverdueCnt, "totalOverdueCnt must not be null");
    }

    public static RentalCardOutputDTO from(
            RentalCard rentalCard
    ) {
        return RentalCardOutputDTO.builder()
                .rentalCardId(rentalCard.getRentalCardNo().getNo())
                .memberId(rentalCard.getMember().getId())
                .memberName(rentalCard.getMember().getName())
                .rentStatus(rentalCard.getRentStatus().toString())
                .totalLateFee(rentalCard.getLateFee().getPoint())
                .totalRentalCnt(rentalCard.totalRentalCnt())
                .totalReturnCnt(rentalCard.totalReturnCnt())
                .totalOverdueCnt(rentalCard.totalOverdueCnt())
                .build();
    }
}
