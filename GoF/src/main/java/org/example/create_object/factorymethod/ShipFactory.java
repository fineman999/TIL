package org.example.create_object.factorymethod;

import java.util.Objects;

public class ShipFactory {
    public static Ship orderShip(String name, String country) {
        checkValid(name, country);


        Ship ship = new Ship();
        ship.setName(name);

        if (name.equalsIgnoreCase("Whiteship")) {
           ship.setLogo("WhiteColor");
          ship.setCountry(country);
        } else if (name.equalsIgnoreCase("Blackship")) {
            ship.setLogo("BlackColor");
            ship.setCountry(country);
        }

        return ship;
    }

    private static void checkValid(String name, String country) {
        if (Objects.isNull(name) || name.isBlank()){
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (Objects.isNull(country) || country.isBlank()){
            throw new IllegalArgumentException("country must not be null or blank");
        }
    }
}
