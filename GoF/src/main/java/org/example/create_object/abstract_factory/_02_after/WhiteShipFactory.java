package org.example.create_object.abstract_factory._02_after;

import org.example.create_object.factorymethod._02_after.Ship;
import org.example.create_object.factorymethod._02_after.ShipFactory;
import org.example.create_object.factorymethod._02_after.WhiteShip;

public class WhiteShipFactory implements ShipFactory {
    private final ShipPartsFactory shipPartsFactory;

    public WhiteShipFactory(ShipPartsFactory shipPartsFactory) {
        this.shipPartsFactory = shipPartsFactory;
    }

    @Override
    public Ship createShip() {
        Ship ship = new WhiteShip();
        ship.setAnchor(shipPartsFactory.createAnchor());
        ship.setCabin(shipPartsFactory.createCabin());
        return ship;
    }
}
