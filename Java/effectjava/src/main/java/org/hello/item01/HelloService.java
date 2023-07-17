package org.hello.item01;

public interface HelloService {
    String hello(String name);

    static HelloService of(String lang) {
        if (lang.equals("ko")) {
            return new KoreanHelloService();
        }
        return new EnglishHelloService();
    }
}
