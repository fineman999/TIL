package org.hello.chapter05.item28.erasure;

import java.util.ArrayList;
import java.util.List;

public class MyGeneric {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("hello");
        String name = names.get(0);
        System.out.println(name);

        // 컴파일 시에 코드 변화
//        List names = new ArrayList(); // 1. 제네릭이 사라지게 된다.
//        names.add("hello"); // 2. 오브젝트 타입인 것은 모두 허용한다.
//        Object o = names.get(0); // 3. 오브젝트 타입으로 값을 꺼낸다.
//        String name = (String) o; // 4. 제네릭 타입을 보고 컴파일러가 캐스팅을 한다.
//        System.out.println(name); // 5. 출력은 hello가 된다.
    }
}
