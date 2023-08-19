package org.hello.chapter05.item28.erasure;

import java.util.ArrayList;
import java.util.List;

public class IntegerToString {
    public static void main(String[] args) {
        // 공변
        Object[] anything = new String[10];
        anything[0] = 1; // 잘못된 형변환이지만 컴파일러가 잡지 못한다.

        // 불공변
        List<String> names = new ArrayList<>();
        // List<Object> objects = names; // 컴파일 에러
    }
}
