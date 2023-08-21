package org.hello.chapter07.item43;

import java.util.function.Function;

public class UnLimitedClass {
    public static void main(String[] args) {
        Function<String, String> function = String::toLowerCase;
        String result = function.apply("HELLO");
        System.out.println(result);
    }
}
