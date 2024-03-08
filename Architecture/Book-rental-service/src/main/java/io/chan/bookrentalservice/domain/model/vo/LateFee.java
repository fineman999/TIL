package io.chan.bookrentalservice.domain.model.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LateFee {
    private final Long point;

    public LateFee addPoint(Long point) {
        return new LateFee(this.point + point);
    }

    public LateFee subtractPoint(Long point) {
        if (this.point - point < 0) {
            throw new IllegalArgumentException("The point cannot be negative.");
        }
        return new LateFee(this.point - point);
    }

    public static LateFee createLateFee(Long point) {
        return new LateFee(point);
    }
}
