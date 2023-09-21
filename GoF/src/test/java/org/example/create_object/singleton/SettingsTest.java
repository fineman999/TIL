package org.example.create_object.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettingsTest {


    @Test
    @DisplayName("basicSettings1과 basicSettings2의 인스턴스는 같지 않다.")
    void test() {
        BasicSettings basicSettings1 = new BasicSettings();
        BasicSettings basicSettings2 = new BasicSettings();

        assertThat(basicSettings1).isNotSameAs(basicSettings2);
    }

    @Test
    @DisplayName("staticSettings1과 staticSettings2의 인스턴스는 같다.")
    void test2() {
        StaticSettings staticSettings1 = StaticSettings.getInstance();
        StaticSettings staticSettings2 = StaticSettings.getInstance();

        assertThat(staticSettings1).isSameAs(staticSettings2);
    }
}