package org.hello.chapter05.item26.unbounded;

import java.util.Set;

public class Numbers {
    static int numElementsInCommon(Set<?> s1, Set<?> s2) {
        s1.add(null); // s1에 null을 추가할 수 있다.
        int result = 0;
        for (Object o1 : s1)
            if (s2.contains(o1))
                result++;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(numElementsInCommon(Set.of(1, 2, 3), Set.of(3, 4, 5)));
    }
}
