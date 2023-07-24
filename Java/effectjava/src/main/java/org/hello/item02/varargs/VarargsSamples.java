package org.hello.item02.varargs;

import java.util.Arrays;

public class VarargsSamples {
    public void printNumbers(int hi, int... numbers) {
        System.out.println(numbers.getClass().getCanonicalName()); // int[]
        System.out.println(numbers.getClass().getComponentType()); // int
        Arrays.stream(numbers).forEach(System.out::println);
    }

    public static void main(String[] args) {
        VarargsSamples varargsSamples = new VarargsSamples();
        varargsSamples.printNumbers(1, 2, 3, 4, 5);
    }
}
