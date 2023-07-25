package org.hello.item03.methodrefrence;

import java.time.LocalDate;

public class Person {
    LocalDate birthday;

    public Person() {
    }

    public Person(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return LocalDate.now().getYear() - birthday.getYear();
    }

    public static int compareByAge(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }

    public int compareByAgeWithInstance(Person a, Person b) {
        return a.birthday.compareTo(b.birthday);
    }

    public int compareByAgeWithMe(Person b) {
        return this.birthday.compareTo(b.birthday);
    }

}
