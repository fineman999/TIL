package org.hello.item10;

import org.hello.chapter02.item10.CaseInsensitiveString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CaseInsensitiveStringTest {

    @Test
    @DisplayName("대칭성 위배 테스트")
    void equals() {
        CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
        String s = "polish";

        assertThat(cis.equals(s)).isTrue();
        assertThat(s.equals(cis)).isFalse();
        List<CaseInsensitiveString> list = new ArrayList<>();
        list.add(cis);
        assertThat(list.contains(s)).isFalse();
    }
}