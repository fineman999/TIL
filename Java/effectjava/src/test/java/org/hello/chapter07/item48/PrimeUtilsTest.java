package org.hello.chapter07.item48;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrimeUtilsTest {

    @Test
    @DisplayName("소수의 개수를 구한다. - parallel x")
    void test() {
        long pi = PrimeUtils.pi(10000000);

        assertThat(pi).isEqualTo(664579);
    }

    @Test
    @DisplayName("소수의 개수를 구한다. - parallel o")
    void test2() {
        long pi = PrimeUtils.parallelPi(10000000);

        assertThat(pi).isEqualTo(664579);
    }

}