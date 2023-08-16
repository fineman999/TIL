package org.hello.chapter04.item24.anonymousclass;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

public class IntArrays {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        // 다이아몬드 연산자를 이렇게 사용하는 건 자바 9부터 가능하다.
        // 자바 9 이전에는 <Integer>를 명시해야 한다.
        return new AbstractList<>() {
            @Override
            public Integer get(int index) {
                return a[index]; // 오토박싱(아이템 6)
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public Integer set(int i, Integer val) {
                int oldVal = a[i];
                a[i] = val; // 오토언박싱
                return oldVal; // 오토박싱
            }
        };
    }
}
