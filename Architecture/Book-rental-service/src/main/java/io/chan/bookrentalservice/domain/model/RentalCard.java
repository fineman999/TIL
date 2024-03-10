package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.domain.model.vo.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@EqualsAndHashCode
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
        final LateFee lateFee = LateFee.LATE_FEE_ZERO;
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

    public RentalCard rentItem(Item item) {
        checkRentalAvailable();
        final RentalItem rentalItem = RentalItem.createRentalItem(item, LocalDateTime.now());
        addRentItem(rentalItem);
        return this;
    }

    // 반납시 연체료를 계산한다.
    public RentalCard returnItem(Item item, LocalDateTime returnDate) {
        final RentalItem rentalItem = rentalItems.stream()
                .filter(tempItem -> tempItem.equalItem(item))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("대여한 책이 아닙니다."));

        calculateLateFee(returnDate, rentalItem);

        final ReturnItem returnItem = ReturnItem.createReturnItem(rentalItem, returnDate);
        addReturnItem(returnItem);
        removeRentItem(rentalItem);
        return this;
    }

    // 배치 형태로 어떤 날짜 기준으로 해당 item 연체기록 쌓기
    public RentalCard overdueItem(Item item) {
        final RentalItem rentalItem = rentalItems.stream()
                .filter(tempItem -> tempItem.equalItem(item))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("대여한 책이 아닙니다."));

        rentalItem.setOverdue(true);
        rentStatus = RentStatus.RENT_UNAVAILABLE;
        return this;
    }

    public Long makeAvailableRental(Long point) {
        if (!rentalItems.isEmpty()) {
            throw new IllegalArgumentException("아직 반납하지 않은 책이 있습니다.");
        }
        if (lateFee.comparePoint(point) < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.lateFee = lateFee.subtractPoint(point);
        if (rentalItems.isEmpty()) {
            rentStatus = RentStatus.RENT_AVAILABLE;
        }
        return this.lateFee.getPoint();
    }

    public Long totalRentalCnt() {
        return (long) rentalItems.size();
    }

    public Long totalReturnCnt() {
        return (long) returnItems.size();
    }

    public Long totalOverdueCnt() {
        return rentalItems.stream().filter(RentalItem::isOverdue).count();
    }

    // 1권이라도 연체되면 사용자는 대여 불가 상태가 된다.
    // 1인당 5권 이내로 지정한다.
    private void checkRentalAvailable() {
        if (rentalItems.size() >= 5) {
            throw new IllegalArgumentException("이미 5권을 대여하셨습니다.");
        }
        if (rentStatus == RentStatus.RENT_UNAVAILABLE) {
            throw new IllegalArgumentException("대여 불가 상태입니다.");
        }
    }
    private void calculateLateFee(final LocalDateTime returnDate, final RentalItem rentalItem) {
        final int days = rentalItem.calculateOverDueDays(returnDate);
        this.lateFee = this.lateFee.calculateLateFee(days);
    }

    private void addRentItem(RentalItem rentalItem) {
        rentalItems.add(rentalItem);
    }

    private void removeRentItem(RentalItem rentalItem) {
        rentalItems.remove(rentalItem);
    }

    private void addReturnItem(ReturnItem returnItem) {
        returnItems.add(returnItem);
    }

    public int rentalItemCount() {
        return rentalItems.size();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("도서 카드: " + member.getName() + "님의 대여 목록");
        joiner.add("대여 도서 연체 상태:" + rentalItems.stream().map(RentalItem::isOverdue).toList());
        joiner.add("총 연체료: " + lateFee.getPoint());
        joiner.add("대여 가능 여부: " + rentStatus.toString());
        joiner.add("대여 목록");
        joiner.add("-" + rentalItems.stream().map(m->m.getItem().getTitle()).toList());
        joiner.add("반납 목록");
        joiner.add("-" + returnItems.stream().map(m->m.getRentalItem().getItem().getTitle()).toList());
        return joiner.toString();
    }
}
