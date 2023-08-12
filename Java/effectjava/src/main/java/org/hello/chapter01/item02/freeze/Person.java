package org.hello.chapter01.item02.freeze;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private final String name;

    private final int age;

    private final List<String> kids;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.kids = new ArrayList<>();
    }
}
