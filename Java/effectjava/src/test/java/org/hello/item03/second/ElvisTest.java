package org.hello.item03.second;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ElvisTest {

    @Test
    @DisplayName("클라이언트 코드가 API를 바꾸지 않고 행위를 바꿀 수 있다.")
    void create() {
        Elvis elvis = Elvis.getInstance();
        elvis.leaveTheBuilding();
        Assertions.assertAll(
                ()-> assertThat(elvis).isEqualTo(Elvis.getInstance()),
                ()-> assertThat(elvis).isSameAs(Elvis.getInstance())
        );
    }

    @Test
    @DisplayName("정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다")
    void generic() {
        MetaElvis<String> elvis1 = MetaElvis.getInstance();
        MetaElvis<Integer> elvis2 = MetaElvis.getInstance();
        System.out.println(elvis1);
        System.out.println(elvis2);
        elvis1.say("hello");
        elvis2.say(100);
    }
}