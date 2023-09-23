package org.example.create_object.factorymethod;

import org.example.create_object.factorymethod._01_before.Ship;
import org.example.create_object.factorymethod._01_before.ShipFactory;
import org.example.create_object.factorymethod._02_after.BlackShipFactory;
import org.example.create_object.factorymethod._02_after.WhiteShipFactory;
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

    @Test
    @DisplayName("client code can create a ship - factory method")
    void client_factory_method() {

        var whiteship = new WhiteShipFactory().orderShip("Whiteship", "Korea");
        assertThat("Ship[name='Whiteship', country='Korea', logo='WhiteColor']").isEqualTo(whiteship.toString());

        var blackship = new BlackShipFactory().orderShip("Blackship", "Korea");
        assertThat("Ship[name='Blackship', country='Korea', logo='BlackColor']").isEqualTo(blackship.toString());

    }
}