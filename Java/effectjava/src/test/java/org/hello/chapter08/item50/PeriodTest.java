package org.hello.chapter08.item50;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodTest {

    @Test
    @DisplayName("Period 인스턴스의 내부를 변경할 수 있다.")
    void test() {
        Date start = new Date(2019, Calendar.FEBRUARY, 1);
        Date end = new Date(2023, Calendar.FEBRUARY, 1);
        Period period = new Period(start, end);

        assertThat(period.end().getYear()).isEqualTo(2023);

        period.end().setYear(2021);

        assertThat(period.end().getYear()).isEqualTo(2021);
    }
}