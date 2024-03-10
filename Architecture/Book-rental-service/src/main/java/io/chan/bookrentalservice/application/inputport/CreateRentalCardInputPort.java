package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.CreateRentalCardUseCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRentalCardInputPort implements CreateRentalCardUseCase {

    private final RentalCardOutputPort rentalCardOutputPort;

    @Transactional
    @Override
    public RentalCardOutputDTO createRentalCard(final UserInputDTO owner) {
        final IDName idName = IDName.createIDName(owner.id(), owner.name());
        RentalCard rentalCard = RentalCard.createRentalCard(idName);
        rentalCard = rentalCardOutputPort.save(rentalCard);
        return RentalCardOutputDTO.from(rentalCard);
    }
}
