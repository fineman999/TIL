package io.chan.bookrentalservice.application.outputport;

import io.chan.bookrentalservice.domain.model.RentalCard;

import java.util.Optional;

public interface RentalCardOutputPort {
    Optional<RentalCard> loadRentalCard(String userId);
    RentalCard save(RentalCard rentalCard);
}
