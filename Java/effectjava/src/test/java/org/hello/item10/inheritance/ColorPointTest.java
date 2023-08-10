package org.hello.item10.inheritance;

import org.hello.item10.Color;
import org.hello.item10.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ColorPointTest {

    @Test
    @DisplayName("대칭성 위배 테스트")
    void equals() {
        Point point = new Point(1, 2);

        ColorPoint colorPoint = new ColorPoint(1, 2, Color.RED);

        assertAll(
                () -> assertThat(point.equals(colorPoint)).isTrue(),
                () -> assertThat(colorPoint.equals(point)).isFalse()
        );
    }

    @Test
    @DisplayName("추이성 위배 테스트")
    void test() {
        ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);

        ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
        assertAll(
                () -> assertThat(p1.equals(p2)).isTrue(),
                () -> assertThat(p2.equals(p3)).isTrue(),
                () -> assertThat(p1.equals(p3)).isFalse()
        );

    }

    @Test
    @DisplayName("equals 규약 위배 테스트")
    void equals2() {
        long time = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(time);
        Date date = new Date(time);

        assertAll(
                () -> assertThat(timestamp.equals(date)).isFalse(),
                () -> assertThat(date.equals(timestamp)).isTrue()
        );
    }
}