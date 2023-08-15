package org.hello.chapter04.item20.skeleton;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

// 코드 20-1 골격 구현을 사용해 완성한 구체 클래스 (133쪽)
public class IntArrays {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        // 다이아몬드 연산자를 이렇게 사용하는 건 자바 9부터 가능하다.
        // 더 낮은 버전을 사용한다면 <Integer>로 수정하자.
        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                return a[index];  // 오토박싱(아이템 6)
            }

            @Override
            public Integer set(int index, Integer e) {
                int oldVal = a[index];
                a[index] = e;      // 오토언박싱
                return oldVal;     // 오토박싱
            }
            @Override
            public int size() {
                return a.length;
            }
        };
    }
}
