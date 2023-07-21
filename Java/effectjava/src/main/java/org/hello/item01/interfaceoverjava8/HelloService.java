package org.hello.item01.interfaceoverjava8;

public interface HelloService {
    String hello();

    default String hi() {
        return "hi";
    }

    static String bye() {
        prepareMessage();
        return "bye";
    }

    static String bye2() {
        prepareMessage();
        return "bye~~";
    }

    static private void prepareMessage() {
    }

}
