package org.hello.chapter05.item31;

import org.hello.chapter05.item31.example.IntegerBox;

import java.util.ArrayList;
import java.util.List;

// 와일드카드 타입을 이용해 재귀적 타입 한정을 표현
public class RecursiveTypeBound {
    public static <E extends Comparable<? super E>> E max(List<? extends E> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("컬렉션이 비어 있습니다.");
        }

        E result = null;
        for (E e: list) {
            if (result == null || e.compareTo(result) > 0) {
                result = e;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<IntegerBox> list = new ArrayList<>();
        list.add(new IntegerBox(1, "first"));
        list.add(new IntegerBox(2, "second"));
        System.out.println(max(list));
    }
}
