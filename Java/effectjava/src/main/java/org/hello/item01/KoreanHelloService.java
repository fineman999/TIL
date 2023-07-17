package org.hello.item01;

public class KoreanHelloService implements HelloService {
    @Override
    public String hello(String name) {
        return "안녕하세요, " + name;
    }
}
