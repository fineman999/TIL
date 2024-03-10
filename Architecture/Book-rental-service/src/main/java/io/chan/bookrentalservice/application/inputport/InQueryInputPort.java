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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InQueryInputPort implements InQueryUseCase {
    private final RentalCardOutputPort rentalCardOutputPort;

    @Override
    public Optional<RentalCardOutputDTO> getRentalCard(final UserInputDTO userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.id())
                .map(RentalCardOutputDTO::from);
    }

    @Override
    public Optional<List<RentItemOutputDTO>> getAllRentItem(final UserInputDTO userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.id())
                .map(loadCard -> loadCard.getRentalItems()
                    .stream()
                    .map(RentItemOutputDTO::from)
                    .toList());
    }

    @Override
    public Optional<List<ReturnItemOutputDTO>> getAllReturnItem(final UserInputDTO userInputDTO) {
        return rentalCardOutputPort.loadRentalCard(userInputDTO.id())
                .map(loadCard -> loadCard.getReturnItems()
                    .stream()
                    .map(ReturnItemOutputDTO::from)
                    .toList());
    }
}
