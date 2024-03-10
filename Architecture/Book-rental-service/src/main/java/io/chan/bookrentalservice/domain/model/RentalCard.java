package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.LateFee;
import io.chan.bookrentalservice.domain.model.vo.RentStatus;
import io.chan.bookrentalservice.domain.model.vo.RentalCardNo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalCard {
    private List<RentalItem> rentalItems;
    private List<ReturnItem> returnItems;
    private RentalCardNo rentalCardNo;
    private IDName member;
    private RentStatus rentStatus;
    private LateFee lateFee;

    public static RentalCard createRentalCard(
            IDName creator
    ) {

        final RentalCardNo cardNo = RentalCardNo.createRentalCardNo(
                UUID.randomUUID(),
                LocalDateTime.now()
        );
        final LateFee lateFee = LateFee.createLateFee(0L);
        final RentStatus rentStatus = RentStatus.RENT_AVAILABLE;
        return new RentalCard(
                new ArrayList<>(),
                new ArrayList<>(),
                cardNo,
                creator,
                rentStatus,
                lateFee
        );
    }

    public void addRentItem(RentalItem rentalItem) {
        rentalItems.add(rentalItem);
    }

    public void removeRentItem(RentalItem rentalItem) {
        rentalItems.remove(rentalItem);
    }

    public void addReturnItem(ReturnItem returnItem) {
        returnItems.add(returnItem);
    }

    public void removeReturnItem(ReturnItem returnItem) {
        returnItems.remove(returnItem);
    }

    public int rentalItemCount() {
        return rentalItems.size();
    }

    public int returnItemCount() {
        return returnItems.size();
    }
}
