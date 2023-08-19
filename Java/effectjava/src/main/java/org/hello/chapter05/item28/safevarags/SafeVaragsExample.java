package org.hello.chapter05.item28.safevarags;

import java.util.List;

public class SafeVaragsExample {

    static void notSafe(List<String>... stringLists) {
        Object[] objects = stringLists; // List<String>... => List[]로 컴파일 된다.(공변)
        List<Integer> tempList = List.of(42);
        objects[0] = tempList; // Semantically invalid, but compiles without warnings
        String s = stringLists[0].get(0); // ClassCastException - 문자열인줄 알았지만 문자열이아니다.
    }

    @SafeVarargs
    static <T> void safe(T... values) {
        for (T value : values) {
            System.out.println(value);
        }
    }

    public static void main(String[] args) {
        // notSafe(List.of("hello", "world"));
        safe(List.of("hello", "world"));
    }
}
