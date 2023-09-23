package org.example.create_object.abstract_factory._02_after;

import org.example.create_object.factorymethod._02_after.Ship;
import org.example.create_object.factorymethod._02_after.ShipFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractFactoryTest {

    @Test
    @DisplayName("WhiteProProFactory can create a ship")
    void name() {
        ShipFactory shipFactory = new WhiteShipFactory(new WhiteProProFactory());
        Ship ship = shipFactory.createShip();
        assertThat(ship.getCabin().getClass().toString()).isEqualTo("class org.example.create_object.abstract_factory._02_after.WhiteCabinPro");
        assertThat(ship.getAnchor().getClass().toString()).isEqualTo("class org.example.create_object.abstract_factory._02_after.WhiteAnchorPro");
    }

    @Test
    @DisplayName("WhiteShipPartsFactory can create a ship")
    void name1() {
        ShipFactory shipFactory = new WhiteShipFactory(new WhiteShipPartsFactory());
        Ship ship = shipFactory.createShip();
        assertThat(ship.getCabin().getClass().toString()).isEqualTo("class org.example.create_object.abstract_factory._01_before.WhiteCabin");
        assertThat(ship.getAnchor().getClass().toString()).isEqualTo("class org.example.create_object.abstract_factory._01_before.WhiteAnchor");
    }
}