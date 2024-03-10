package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.ReturnItemUserCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.vo.Item;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserItemInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReturnItemInputPort implements ReturnItemUserCase {
    private final RentalCardOutputPort rentalCardOutputPort;

    @Transactional
    @Override
    public RentalCardOutputDTO returnItem(final UserItemInputDTO returnItemInputDTO) {
        // OutputPort 를 통해 대여카드를 조회하고 없으면 예외를 발생시킨다.
        final RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(returnItemInputDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 대여카드가 존재하지 않습니다."));

        Item returnItem = Item.createItem(returnItemInputDTO.itemId(), returnItemInputDTO.itemTitle());
        rentalCard.returnItem(returnItem, LocalDateTime.now());

        return RentalCardOutputDTO.from(rentalCard);
    }
}
