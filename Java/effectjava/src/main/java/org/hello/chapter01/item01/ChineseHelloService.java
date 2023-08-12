package org.hello.chapter01.item01;

public class ChineseHelloService implements HelloService {
    @Override
    public String hello(String name) {
        return "你好 " + name;
    }
}
