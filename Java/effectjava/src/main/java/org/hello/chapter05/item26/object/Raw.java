package org.hello.chapter05.item26.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// 코드 26-4 런타임에 실패한다. - unsafeAdd 메서드가 로 타입(List)을 사용 (156쪽)
public class Raw {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0); // 컴파일러가 자동으로 형변환 코드를 넣어준다.
    }

    private static void unsafeAdd(List list, Object o) {
        list.add(o); // 컴파일러가 자동으로 형변환 코드를 넣어준다.
    }
}
