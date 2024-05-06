package io.chan.globalutils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AndOrUtilsTest {

    @Test
    void or() {
        assertThat(AndOrUtils.or(true, false)).isTrue();
    }

    @Test
    void and() {
        assertThat(AndOrUtils.and(true, false)).isFalse();
    }
}
