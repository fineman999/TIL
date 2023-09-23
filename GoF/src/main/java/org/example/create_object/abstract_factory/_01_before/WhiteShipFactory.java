package org.example.create_object.abstract_factory._01_before;

import org.example.create_object.factorymethod._02_after.Ship;
import org.example.create_object.factorymethod._02_after.ShipFactory;
import org.example.create_object.factorymethod._02_after.WhiteShip;

public class WhiteShipFactory implements ShipFactory {
    @Override
    public Ship createShip() {
        Ship ship = new WhiteShip();
        ship.setAnchor(new WhiteAnchor());
        ship.setCabin(new WhiteCabin());
        return ship;
    }
}
