package org.hello.chapter07.item46;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {


    @Test
    @DisplayName("빈도표에서 가장 흔한 단어 10개를 뽑아내는 파이프라인")
    void test() {
        Map<String, Long> freq = new HashMap<>();
        freq.put("hello", 1L);
        freq.put("world", 2L);
        freq.put("java", 3L);
        freq.put("python", 4L);
        freq.put("c++", 5L);
        freq.put("c#", 6L);
        freq.put("c", 7L);
        freq.put("kotlin", 8L);
        freq.put("scala", 9L);
        freq.put("groovy", 10L);

        List<String> list = freq.keySet().stream()
                .sorted(Comparator.comparing(freq::get).reversed())
                .limit(10)
                .toList();

        List<String> expected = List.of("groovy", "scala", "kotlin", "c", "c#", "c++", "python", "java", "world", "hello");

        assertThat(list).isEqualTo(expected);
    }

    @Test
    @DisplayName("collector factory: toList")
    void toList() {
        List<Integer> integers = Stream.of(1, 2, 3, 4, 5).toList();

        assertThat(integers).containsExactly(1, 2, 3, 4, 5);

    }

    @Test
    @DisplayName("collector factory: toSet")
    void toSet() {
        Set<String> uniqueNames = Stream.of("Alice", "Bob", "Alice", "Charlie")
                .collect(Collectors.toSet());

        assertThat(uniqueNames).contains("Alice", "Bob", "Charlie");

    }

    @Test
    @DisplayName("collector factory: tomap")
    void toCollection() {
        Person alice = new Person("Alice", 23, "Korea");
        Person bob = new Person("Bob", 25, "USA");
        Person charlie = new Person("Charlie", 27, "Japan");
        List<Person> people = List.of(alice, bob, charlie);

        Map<String, Integer> nameToAgeMap = people.stream()
                .collect(Collectors.toMap(Person::name, Person::age));

        assertThat(nameToAgeMap).containsEntry("Alice", 23);
        assertThat(nameToAgeMap).containsEntry("Bob", 25);
        assertThat(nameToAgeMap).containsEntry("Charlie", 27);
    }
    private record Person(String name, int age, String country) {
    }

    @Test
    @DisplayName("collector factory: groupingBy")
    void groupingBy() {
        Person alice = new Person("Alice", 23, "Korea");
        Person bob = new Person("Bob", 25, "USA");
        Person charlie = new Person("Charlie", 27, "Korea");
        List<Person> people = List.of(alice, bob, charlie);

        Map<String, List<Person>> collect = people.stream()
                .collect(Collectors.groupingBy(Person::country));

        assertThat(collect.get("Korea")).containsExactly(alice, charlie);
        assertThat(collect.get("USA")).containsExactly(bob);
    }

    @Test
    @DisplayName("collector factory: joining")
    void joining() {
        Person alice = new Person("Alice", 23, "Korea");
        Person bob = new Person("Bob", 25, "USA");
        Person charlie = new Person("Charlie", 27, "Korea");

        List<Person> people = List.of(alice, bob, charlie);

        String names = people.stream()
                .map(Person::name)
                .collect(Collectors.joining(", "));

        assertThat(names).isEqualTo("Alice, Bob, Charlie");
    }

}
