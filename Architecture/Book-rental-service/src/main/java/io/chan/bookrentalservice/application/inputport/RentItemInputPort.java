package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.EventOutputPort;
import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.RentItemUseCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.event.ItemRented;
import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.Item;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserItemInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 도서 대여시 도서대여됨 이벤트 발행
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RentItemInputPort implements RentItemUseCase {
    private final RentalCardOutputPort rentalCardOutputPort;
    private final EventOutputPort eventOutputPort;

    @Override
    public RentalCardOutputDTO rentItem(final UserItemInputDTO rental) {
        RentalCard rentalCard = rentalCardOutputPort.loadRentalCard(rental.userId())
                .orElseGet(() -> RentalCard.createRentalCard(IDName.createIDName(rental.userId(), rental.userName())));
        final Item newItem = Item.createItem(rental.itemId(), rental.itemTitle());
        rentalCard.rentItem(newItem);

        final ItemRented itemRentedEvent = RentalCard.createItemRentedEvent(rentalCard.getMember(), newItem, 10L);
        eventOutputPort.occurRentalEvent(itemRentedEvent);

        rentalCard = rentalCardOutputPort.save(rentalCard);
        return RentalCardOutputDTO.from(rentalCard);
    }
}
