package org.hello.item01;

import java.util.Optional;
import java.util.ServiceLoader;

public class HelloServiceFactory {

    public static void main(String[] args) {
        // 첫 번째 코드
        ServiceLoader<HelloService> loader = ServiceLoader.load(HelloService.class);
        Optional<HelloService> helloServiceOptional = loader.findFirst();
        helloServiceOptional.ifPresent(helloService ->
                System.out.println(helloService.hello("world")));

        // 두 번째 코드
        HelloService helloService = new ChineseHelloService();
        System.out.println(helloService.hello("world"));
    }
}
