package io.chan.userservice.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Point {
    @Column(name = "point")
    private Long pointValue;

    public static Point createZero() {
        return new Point(0L);
    }

    public Point add(Long value) {
        return new Point(this.pointValue + value);
    }

    public Point subtract(Long value) {
        validate(value);
        return new Point(this.pointValue - value);
    }

    private void validate(final Long value) {
        if (value < 0) {
            throw new IllegalArgumentException("포인트는 0보다 작을 수 없습니다.");
        }
        if (this.pointValue - value < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }
}
