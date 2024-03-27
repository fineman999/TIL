package io.chan.bookrentalservice.application.outputport;

import io.chan.bookrentalservice.domain.model.event.ItemRented;
import io.chan.bookrentalservice.domain.model.event.ItemReturned;
import io.chan.bookrentalservice.domain.model.event.OverdueCleared;

public interface EventOutputPort {
    void occurRentalEvent(ItemRented rentalItemEvent);
    void occurReturnEvent(ItemReturned itemReturnedEvent);
    void occurOverdueClearedEvent(OverdueCleared overdueCleared);
}
