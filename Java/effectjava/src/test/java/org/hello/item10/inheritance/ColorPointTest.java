package org.hello.item10.inheritance;

import org.hello.chapter02.item10.composition.ColorPoint;
import org.hello.chapter02.item10.Color;
import org.hello.chapter02.item10.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ColorPointTest {

    @Test
    @DisplayName("대칭성 위배 테스트")
    void equals() {
        Point point = new Point(1, 2);

        org.hello.chapter02.item10.inheritance.ColorPoint colorPoint = new org.hello.chapter02.item10.inheritance.ColorPoint(1, 2, Color.RED);

        assertAll(
                () -> assertThat(point.equals(colorPoint)).isTrue(),
                () -> assertThat(colorPoint.equals(point)).isFalse()
        );
    }

    @Test
    @DisplayName("추이성 위배 테스트")
    void test() {
        org.hello.chapter02.item10.inheritance.ColorPoint p1 = new org.hello.chapter02.item10.inheritance.ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);

        org.hello.chapter02.item10.inheritance.ColorPoint p3 = new org.hello.chapter02.item10.inheritance.ColorPoint(1, 2, Color.BLUE);
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

    @Test
    @DisplayName("equals 규약 준수 테스트")
    void equals3() {
        Point p1 = new Point(1, 0);
        Point p2 = new ColorPoint(1, 0, Color.RED).asPoint();

        assertAll(
                () -> assertThat(p1.equals(p2)).isTrue(),
                () -> assertThat(p2.equals(p1)).isTrue()
        );
    }

    @Test
    @DisplayName("일관성 위배 테스트")
    void equals4() throws MalformedURLException {
        URL google1 = new URL("https", "about.google", "/products/");
        URL google2 = new URL("https", "about.google", "/products/");

        assertAll(
                () -> assertThat(google1.equals(google2)).isTrue(),
                () -> assertThat(google2.equals(google1)).isTrue()
        );
    }
}