package org.hello.item02.hierarchicalbuilder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PizzaTest {

    @Test
    @DisplayName("뉴욕 피자를 만든다.")
    void create() {
        NyPizza pizza = new NyPizza.Builder(NyPizza.Size.SMALL)
                .addTopping(Pizza.Topping.SAUSAGE)
                .addTopping(Pizza.Topping.ONION)
                .build();
        assertThat(pizza.toString())
                .isEqualTo("[ONION, SAUSAGE]로 토핑한 피자 사이즈: SMALL");
    }

    @Test
    @DisplayName("칼초네 피자를 만든다.")
    void createCalzone() {
        Calzone calzone = new Calzone.Builder()
                .addTopping(Pizza.Topping.HAM)
                .sauceInside()
                .build();
        System.out.println(calzone);
        assertThat(calzone.toString()).isEqualTo("[HAM]로 토핑한 칼초네 피자 (소스는 안에)");
    }
}