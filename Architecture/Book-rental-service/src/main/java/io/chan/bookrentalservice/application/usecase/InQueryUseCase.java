package io.chan.bookrentalservice.application.usecase;

import io.chan.bookrentalservice.framework.web.dto.RentItemOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.ReturnItemOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;

import java.util.List;

public interface InQueryUseCase {
    RentalCardOutputDTO getRentalCard(UserInputDTO userInputDTO);
    List<RentItemOutputDTO> getAllRentItem(UserInputDTO userInputDTO);
    List<ReturnItemOutputDTO> getAllReturnItem(UserInputDTO userInputDTO);
}
