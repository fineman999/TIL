package org.hello.item03.methodrefrence;

import org.hello.chapter01.item03.methodrefrence.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class PersonTest {


    @Test
    @DisplayName("new Comparator을 이용하여 Person의 birthday를 비교한다.")
    void compare() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(LocalDate.of(1982, 7, 15)));
        people.add(new Person(LocalDate.of(2011, 3, 2)));
        people.add(new Person(LocalDate.of(2013, 1, 28)));

        people.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.birthday.compareTo(o2.birthday);
            }
        });

        people.forEach(System.out::println);
    }
    @Test
    @DisplayName("Lambda을 이용하여 Person의 birthday를 비교한다.")
    void compareLambda() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(LocalDate.of(1982, 7, 15)));
        people.add(new Person(LocalDate.of(2011, 3, 2)));
        people.add(new Person(LocalDate.of(2013, 1, 28)));

        people.sort((o1, o2) -> o1.birthday.compareTo(o2.birthday));

        people.forEach(System.out::println);
    }

    @Test
    @DisplayName("스태틱 메소드 레퍼런스를 이용하여 Person의 birthday를 비교한다.")
    void methodReference() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(LocalDate.of(1982, 7, 15)));
        people.add(new Person(LocalDate.of(2011, 3, 2)));
        people.add(new Person(LocalDate.of(2013, 1, 28)));

        people.sort(Person::compareByAge);

        people.forEach(System.out::println);
    }

    @Test
    @DisplayName("인스턴스 메소드 레퍼런스를 이용하여 Person의 birthday를 비교한다.")
    void instanceMethodReference() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(LocalDate.of(1982, 7, 15)));
        people.add(new Person(LocalDate.of(2011, 3, 2)));
        people.add(new Person(LocalDate.of(2013, 1, 28)));

        Person person = new Person(null);
        people.sort(person::compareByAgeWithInstance);
    }


    @Test
    @DisplayName("임의 객체의 인스턴스 메소드 레퍼런스를 이용하여 Person의 birthday를 비교한다.")
    void arbitraryInstanceMethodReference() {
        List<Person> people = new ArrayList<>();
        people.add(new Person(LocalDate.of(1982, 7, 15)));
        people.add(new Person(LocalDate.of(2011, 3, 2)));
        people.add(new Person(LocalDate.of(2013, 1, 28)));

        people.sort(Person::compareByAgeWithMe);
    }

    @Test
    @DisplayName("생성자 레퍼런스를 이용하여 person을 생성한다.")
    void constructorReference() {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.of(1982, 7, 15));
        dates.add(LocalDate.of(2011, 3, 2));
        dates.add(LocalDate.of(2013, 1, 28));

        Function<LocalDate, Person> aNew = Person::new;
        dates.stream().map(aNew).collect(Collectors.toList());

    }

    @Test
    @DisplayName("함수형 인터페이스")
    void functionalInterface() {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.of(1982, 7, 15));
        dates.add(LocalDate.of(2011, 3, 2));
        dates.add(LocalDate.of(2013, 1, 28));

        Predicate<LocalDate> localDatePredicate = date -> date.isBefore(LocalDate.of(2000, 1, 1));
        Function<LocalDate, Integer> getYear = LocalDate::getYear;
        List<Integer> before2000 = dates.stream()
                .filter(localDatePredicate)
                .map(getYear)
                .collect(Collectors.toList());

    }
}