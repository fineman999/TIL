package org.hello.item01;

public class HelloServiceFactory {

    public static void main(String[] args) {
        HelloService helloService = HelloService.of("ko");
        System.out.println(helloService.hello("김철수"));
    }
}
