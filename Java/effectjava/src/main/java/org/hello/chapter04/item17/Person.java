package org.hello.chapter04.item17;

public class Person {
    private final Address address;

    public Person(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return new Address(address.getZipCode(), address.getStreet(), address.getCity());
    }


}
