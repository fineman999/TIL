package org.example.create_object.factorymethod._02_after;

public class Client {

    public Ship print(ShipFactory shipFactory, String name, String country) {
        return shipFactory.orderShip(name, country);
    }
}
