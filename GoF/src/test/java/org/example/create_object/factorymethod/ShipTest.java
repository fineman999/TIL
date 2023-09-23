package org.example.create_object.factorymethod;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    @Test
    @DisplayName("client code can create a ship")
    void client() {

        Ship whiteship = ShipFactory.orderShip("Whiteship", "Korea");
        org.assertj.core.api.Assertions.assertThat("Whiteship").isEqualTo(whiteship.toString());

    }
}