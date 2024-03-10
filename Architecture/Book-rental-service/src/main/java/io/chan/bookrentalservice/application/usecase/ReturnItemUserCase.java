package io.chan.bookrentalservice.application.usecase;

import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserItemInputDTO;

public interface ReturnItemUserCase {
    RentalCardOutputDTO returnItem(UserItemInputDTO returnItemInputDTO);
}
