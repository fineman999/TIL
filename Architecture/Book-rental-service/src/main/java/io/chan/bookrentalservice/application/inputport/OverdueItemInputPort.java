package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.OverdueItemUseCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.vo.Item;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserItemInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OverdueItemInputPort implements OverdueItemUseCase {

    private final RentalCardOutputPort rentalCardOutputPort;

    @Override
    public RentalCardOutputDTO overdueItem(final UserItemInputDTO userItemInputDTO) {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(userItemInputDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 대여카드가 존재하지 않습니다."));

        final Item item = Item.createItem(userItemInputDTO.itemId(), userItemInputDTO.itemTitle());
        rentalCard.overdueItem(item);
        return RentalCardOutputDTO.from(rentalCard);
    }
}
