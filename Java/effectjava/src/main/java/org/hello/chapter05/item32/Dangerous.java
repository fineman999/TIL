package org.hello.chapter05.item32;

import java.util.List;

// 제네릭 varargs 배열 매개변수에 값을 저장하려는 시도는 안전하지 않다.
public class Dangerous {
    // 제네릭 varargs를 혼용하면 타입 안전성이 깨진다.
    static void dangerous(List<String>... stringLists) {
        List<Integer> intList = List.of(42);
        Object[] objects = stringLists; // 배열은 공변이므로 Object[]에 List<String>[]을 할당할 수 있다.
        objects[0] = intList; // 힙 오염 발생 - string Lists의 배열에는 Integer List를 넣을 수 없다.
        String s = stringLists[0].get(0); // ClassCastException
    }

    public static void main(String[] args) {
        dangerous(List.of("There be dragons!"));
    }
}
