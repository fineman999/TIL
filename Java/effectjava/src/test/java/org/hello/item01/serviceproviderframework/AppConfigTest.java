package org.hello.item01.serviceproviderframework;

import org.assertj.core.api.Assertions;
import org.hello.item01.HelloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

class AppConfigTest {

    @Test
    @DisplayName("서비스 접근 API 테스트")
    void test() {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        HelloService helloService = applicationContext.getBean(HelloService.class);

        assertThat(helloService.hello("Spring")).isEqualTo("你好 Spring");
    }
}