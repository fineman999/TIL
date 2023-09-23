package org.example.create_object.factorymethod._02_after;

import java.util.Objects;

public class WhiteShipFactory implements ShipFactory{

    @Override
    public Ship createShip() {
        return new WhiteShip();
    }
}
