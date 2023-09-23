package org.example.create_object.factorymethod._02_after;


import org.example.create_object.abstract_factory._01_before.WhiteAnchor;
import org.example.create_object.abstract_factory._01_before.WhiteCabin;
import org.example.create_object.abstract_factory._02_after.Anchor;
import org.example.create_object.abstract_factory._02_after.Cabin;

import java.util.StringJoiner;

public class Ship {
    private String name;
    private String country;
    private String logo;

    private Anchor anchor;
    private Cabin cabin;

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

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
