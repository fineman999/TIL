package org.hello.item01.serviceproviderframework;

import org.hello.item01.ChineseHelloService;
import org.hello.item01.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public HelloService helloService() {
        return new ChineseHelloService();
    }
}
