package org.hello.item06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hello.item06.RomanNumerals.isRomanNumeralFast;
import static org.hello.item06.RomanNumerals.isRomanNumeralSlow;

class RomanNumeralsTest {

    @Test
    @DisplayName("정규식 성능 테스트")
    void test() {

        long startSlow = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            isRomanNumeralSlow("MCMLXXVI");
        }

        long endSlow = System.nanoTime();

        long startFast = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            isRomanNumeralFast("MCMLXXVI");
        }

        long endFast = System.nanoTime();

        System.out.println("Slow: " + (endSlow - startSlow));
        System.out.println("Fast: " + (endFast - startFast));
        System.out.println("Diff: " + ((endSlow - startSlow) - (endFast - startFast)));

        assertThat(endSlow - startSlow).isGreaterThan(endFast - startFast);
    }
}