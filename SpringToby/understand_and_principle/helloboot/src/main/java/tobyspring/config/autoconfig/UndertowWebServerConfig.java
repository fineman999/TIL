package tobyspring.config.autoconfig;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import tobyspring.config.ConditionalMyOnClass;
import tobyspring.config.MyAutoConfiguration;

@MyAutoConfiguration
@ConditionalMyOnClass("org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory")
public class UndertowWebServerConfig {
    @Bean
    public ServletWebServerFactory undertowServletWebServerFactory() {
        return new UndertowServletWebServerFactory();
    }

}
