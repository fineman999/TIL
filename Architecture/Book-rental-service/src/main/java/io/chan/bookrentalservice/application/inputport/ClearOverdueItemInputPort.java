package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.EventOutputPort;
import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.ClearOverdueItemUseCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.event.OverdueCleared;
import io.chan.bookrentalservice.framework.web.dto.ClearOverdueInfoDto;
import io.chan.bookrentalservice.framework.web.dto.RentalResultOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 정지해제처리시 정지해제됨 이벤트 발행
 */
@Transactional
@Service
@RequiredArgsConstructor
public class ClearOverdueItemInputPort implements ClearOverdueItemUseCase {
    private final RentalCardOutputPort rentalCardOutputPort;
    private final EventOutputPort eventOutputPort;
    @Override
    public RentalResultOutputDTO clearOverdueItem(final ClearOverdueInfoDto clearOverdueInfoDto) {
        final RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(clearOverdueInfoDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 대여카드가 존재하지 않습니다."));

        rentalCard.makeAvailableRental(clearOverdueInfoDto.point());

        final OverdueCleared clearedEvent = RentalCard.createOverdueClearedEvent(rentalCard.getMember(), clearOverdueInfoDto.point());
        eventOutputPort.occurOverdueClearedEvent(clearedEvent);

        return RentalResultOutputDTO.from(rentalCard);
    }
}
