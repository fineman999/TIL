package io.chan.userservice.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class PointTest {

    @DisplayName("Point 객체 생성 테스트")
    @Test
    void createZero() {
        Point point = Point.createZero();
        assertThat(point.getPointValue()).isZero();
    }

    @DisplayName("Point 객체 더하기 테스트")
    @Test
    void add() {
        Point point = Point.createZero();
        Point addedPoint = point.add(100L);
        assertThat(addedPoint.getPointValue()).isEqualTo(100L);
    }

    @DisplayName("Point 객체 빼기 테스트")
    @Test
    void subtract() {
        Point point = Point.createZero();
        Point addedPoint = point.add(100L);
        Point subtractedPoint = addedPoint.subtract(50L);
        assertThat(subtractedPoint.getPointValue()).isEqualTo(50L);
    }
}