package org.hello.chapter04.item20.multipleinheritance;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyCatTest {

    @Test
    void test() {
        MyCat myCat = new MyCat();
        assertThat(myCat.sound()).isEqualTo("meow");
        assertThat(myCat.name()).isEqualTo("유미");
        myCat.fly();
    }
}