package io.chan.bookrentalservice.application.inputport;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.InQueryUseCase;
import io.chan.bookrentalservice.framework.web.dto.RentItemOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.ReturnItemOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InQueryInputPort implements InQueryUseCase {
    private static final String RENTAL_CARD_NOT_FOUND = "Rental card not found";
    private final RentalCardOutputPort rentalCardOutputPort;

    @Override
    public RentalCardOutputDTO getRentalCard(final UserInputDTO userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.id())
                .map(RentalCardOutputDTO::from)
                .orElseThrow(() -> new IllegalArgumentException(RENTAL_CARD_NOT_FOUND));
    }

    @Override
    public List<RentItemOutputDTO> getAllRentItem(final UserInputDTO userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.id())
                .map(loadCard -> loadCard.getRentalItems()
                    .stream()
                    .map(RentItemOutputDTO::from)
                    .toList())
                .orElseThrow(() -> new IllegalArgumentException(RENTAL_CARD_NOT_FOUND));
    }

    @Override
    public List<ReturnItemOutputDTO> getAllReturnItem(final UserInputDTO userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.id())
                .map(loadCard -> loadCard.getReturnItems()
                    .stream()
                    .map(ReturnItemOutputDTO::from)
                    .toList())
                .orElseThrow(() -> new IllegalArgumentException(RENTAL_CARD_NOT_FOUND));
    }
}
