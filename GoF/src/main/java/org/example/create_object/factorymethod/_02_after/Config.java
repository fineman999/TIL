package org.example.create_object.factorymethod._02_after;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public String hello() {
        return "Hello";
    }
}
