package org.hello.item03.functionalinterface;

import org.hello.item03.methodrefrence.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class FunctionalInterfaceTest {
    @Test
    @DisplayName("자바에서 제공하는 기본 함수형 인터페이스")
    void test() {
        Function<Integer, String> intToString = String::valueOf;
        Supplier<String> stringSupplier;
        Consumer<String> stringConsumer = System.out::println;
        Predicate<String> stringPredicate;

        Supplier<Person> personSupplier = Person::new;
        Function<LocalDate, Person> personFunction = Person::new;

    }
}