package io.chan.bookrentalservice.application.usecase;

import io.chan.bookrentalservice.framework.web.dto.RentItemOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.ReturnItemOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;

import java.util.List;
import java.util.Optional;

public interface InQueryUseCase {
    Optional<RentalCardOutputDTO> getRentalCard(UserInputDTO userInputDTO);
    Optional<List<RentItemOutputDTO>> getAllRentItem(UserInputDTO userInputDTO);
    Optional<List<ReturnItemOutputDTO>> getAllReturnItem(UserInputDTO userInputDTO);
}
