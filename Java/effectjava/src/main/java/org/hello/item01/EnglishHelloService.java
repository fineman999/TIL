package org.hello.item01;

public class EnglishHelloService implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
