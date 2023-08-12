package org.hello.chapter02.item10.record;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointTest {

    @Test
    @DisplayName("Record를 이용한 equals 테스트")
    void name() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);

        assertThat(p1.equals(p2)).isTrue();
        System.out.println(p1);
    }
}