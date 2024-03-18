package io.chan.userservice.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Point {
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
