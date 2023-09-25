package org.hello.chapter01.item03.methodrefrence;

import java.time.LocalDate;

public class Person {
    public LocalDate birthday;

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
