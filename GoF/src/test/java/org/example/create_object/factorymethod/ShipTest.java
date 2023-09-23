package org.example.create_object.factorymethod;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShipTest {
    @Test
    @DisplayName("client code can create a ship")
    void client() {

        Ship whiteship = ShipFactory.orderShip("Whiteship", "Korea");
        assertThat("Ship[name='Whiteship', country='Korea', logo='WhiteColor']").isEqualTo(whiteship.toString());

        Ship blackship = ShipFactory.orderShip("Blackship", "Korea");
        assertThat("Ship[name='Blackship', country='Korea', logo='BlackColor']").isEqualTo(blackship.toString());

    }
}