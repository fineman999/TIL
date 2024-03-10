package io.chan.bookrentalservice.application.usecase;

import io.chan.bookrentalservice.framework.web.dto.ClearOverdueInfoDto;
import io.chan.bookrentalservice.framework.web.dto.RentalResultOutputDTO;

public interface ClearOverdueItemUseCase {
    RentalResultOutputDTO clearOverdueItem(ClearOverdueInfoDto clearOverdueInfoDto);
}
