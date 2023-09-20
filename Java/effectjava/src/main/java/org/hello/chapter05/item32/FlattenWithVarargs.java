package org.hello.chapter05.item32;

import java.util.ArrayList;
import java.util.List;

// 제네릭 varargs 가변인자 매개변수를 안전하게 사용하는 메서드
public class FlattenWithVarargs {

    // 제네릭 varargs 배열 가변인자를 List로 대체
    @SafeVarargs
    static <T> List<T> flatten(List<? extends T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list: lists) {
            result.addAll(list);
        }
        return result;
    }

    public static void main(String[] args) {
        List<Integer> flatList = flatten(List.of(1, 2), List.of(3, 4, 5), List.of(6, 7, 8, 9));
        System.out.println(flatList);
    }
}
