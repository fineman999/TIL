package org.hello.chapter05.item26.terms;

import java.util.ArrayList;
import java.util.List;

public class GenericBasic {
    public static void main(String[] args) {
        // Generic 사용하기 전
        List numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add("string!!");
        for (Object number : numbers) {
            System.out.println(number);
        }

        // Generic 사용하기 후
        List<Integer> numbers2 = new ArrayList<>();
        numbers2.add(1);
        // numbers2.add("string!!"); // 컴파일 에러
        for (Integer number : numbers2) {
            System.out.println(number);
        }
    }
}
