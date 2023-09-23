package org.example.create_object.factorymethod;

import org.example.create_object.factorymethod._01_before.Ship;
import org.example.create_object.factorymethod._01_before.ShipFactory;
import org.example.create_object.factorymethod._02_after.BlackShipFactory;
import org.example.create_object.factorymethod._02_after.Client;
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

    @Test
    @DisplayName("client code can create a ship - client에 factory method를 주입(의존성 주입)")
    void client_using_client() {

        Client client = new Client();
        var print = client.print(new WhiteShipFactory(), "Whiteship", "Korea");

        assertThat("Ship[name='Whiteship', country='Korea', logo='WhiteColor']").isEqualTo(print.toString());


        var print1 = client.print(new BlackShipFactory(), "Blackship", "Korea");
        assertThat("Ship[name='Blackship', country='Korea', logo='BlackColor']").isEqualTo(print1.toString());


    }
}