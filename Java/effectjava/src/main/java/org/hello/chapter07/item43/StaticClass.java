package org.hello.chapter07.item43;

import java.util.function.Function;

public class StaticClass {
    public static int square(int x) {
        return x * x;
    }

    public static void main(String[] args) {
        Function<Integer, Integer> squreFunction = StaticClass::square;
        int result = squreFunction.apply(5);
    }
}
