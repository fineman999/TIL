package org.example.create_object.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasicSettingsTest {


    @Test
    @DisplayName("인스턴스 두개는 같지 않다.")
    void test() {
        BasicSettings basicSettings1 = new BasicSettings();
        BasicSettings basicSettings2 = new BasicSettings();

        assertThat(basicSettings1).isNotSameAs(basicSettings2);
    }
}