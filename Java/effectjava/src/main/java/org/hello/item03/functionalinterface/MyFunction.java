package org.hello.item03.functionalinterface;

@FunctionalInterface
public interface MyFunction {
    String valueOf(Integer integer);

    default void say() {
        System.out.println("Hello World!");
    }
}
