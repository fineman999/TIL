package io.chan.bookrentalservice.domain.model.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LateFee {
    private final Long point;
    private static final Long  LATE_FEE_POINT = 10L;
    public static final LateFee LATE_FEE_ZERO = new LateFee(0L);

    public LateFee addPoint(Long point) {
        return new LateFee(this.point + point);
    }

    public LateFee subtractPoint(Long point) {
        if (this.point < point) {
            throw new IllegalArgumentException("The point cannot be negative.");
        }
        return new LateFee(this.point - point);
    }

    public static LateFee createLateFee(Long point) {
        return new LateFee(point);
    }

    public LateFee calculateLateFee(int days) {
        return this.addPoint(days * LATE_FEE_POINT);
    }

    public int comparePoint(final Long point) {
        return this.point.compareTo(point);
    }
}
