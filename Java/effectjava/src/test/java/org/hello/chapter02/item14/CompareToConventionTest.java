package org.hello.chapter02.item14;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CompareToConventionTest {

    @Test
    void name() {
        BigDecimal n1 = BigDecimal.valueOf(23134134);
        BigDecimal n2 = BigDecimal.valueOf(11231230);
        BigDecimal n3 = BigDecimal.valueOf(33134134);
        BigDecimal n4 = BigDecimal.valueOf(11231230);

        // 반사성
        assertThat(n1.compareTo(n1)).isEqualTo(0);
        // 대칭성
        assertThat(n1.compareTo(n2)).isEqualTo(1);
        assertThat(n2.compareTo(n1)).isEqualTo(-1);

        // 추이성
        assertThat(n3.compareTo(n1)).isEqualTo(1);
        assertThat(n1.compareTo(n2)).isEqualTo(1);
        assertThat(n3.compareTo(n2)).isEqualTo(1);

        // 일관성
        assertThat(n4.compareTo(n2)).isEqualTo(0);
        assertThat(n2.compareTo(n1)).isEqualTo(-1);
        assertThat(n4.compareTo(n1)).isEqualTo(-1);

        // compareTo가 0이면 equals가 true (equals는 compareTo가 0이 아닐 수 있음)
        BigDecimal oneZero = new BigDecimal("1.0");
        BigDecimal oneZeroZero = new BigDecimal("1.00");
        assertThat(oneZero.compareTo(oneZeroZero)).isEqualTo(0); // 1.0 == 1.00
        assertThat(oneZero.equals(oneZeroZero)).isFalse(); // 1.0 != 1.00
    }
}