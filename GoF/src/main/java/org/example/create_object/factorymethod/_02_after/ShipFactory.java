package org.example.create_object.factorymethod._02_after;

import java.util.Objects;

public interface ShipFactory {
    default Ship orderShip(String name, String country) {
        checkValid(name, country);

        return createShip();
    }


    Ship createShip();

    private void checkValid(String name, String country) {
        if (Objects.isNull(name) || name.isBlank()){
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (Objects.isNull(country) || country.isBlank()){
            throw new IllegalArgumentException("country must not be null or blank");
        }
    }
}
