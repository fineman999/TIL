package io.chan.bookrentalservice.application.usecase;

import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;

public interface CreateRentalCardUseCase {
    RentalCardOutputDTO createRentalCard(UserInputDTO userInputDTO);
}
