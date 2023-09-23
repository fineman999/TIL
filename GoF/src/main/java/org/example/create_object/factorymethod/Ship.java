package org.example.create_object.factorymethod;


import java.util.StringJoiner;

public class Ship {
    private String name;
    private String country;
    private String logo;

    public Ship(String name, String country, String logo) {
        this.name = name;
        this.country = country;
        this.logo = logo;
    }

    public Ship() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Ship.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("country='" + country + "'")
                .add("logo='" + logo + "'")
                .toString();
    }
}
