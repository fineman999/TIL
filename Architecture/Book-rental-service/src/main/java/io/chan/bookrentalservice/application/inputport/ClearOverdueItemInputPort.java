package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.ClearOverdueItemUseCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.framework.web.dto.ClearOverdueInfoDto;
import io.chan.bookrentalservice.framework.web.dto.RentalResultOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClearOverdueItemInputPort implements ClearOverdueItemUseCase {
    private final RentalCardOutputPort rentalCardOutputPort;
    @Override
    public RentalResultOutputDTO clearOverdueItem(final ClearOverdueInfoDto clearOverdueInfoDto) {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(clearOverdueInfoDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 대여카드가 존재하지 않습니다."));

        rentalCard.makeAvailableRental(clearOverdueInfoDto.point());

        return RentalResultOutputDTO.from(rentalCard);
    }
}
