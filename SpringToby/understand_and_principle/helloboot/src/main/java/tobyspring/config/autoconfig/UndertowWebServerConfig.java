package tobyspring.config.autoconfig;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import tobyspring.config.MyAutoConfiguration;

@MyAutoConfiguration
@Conditional(UndertowWebServerConfig.UndertowCondition.class)
public class UndertowWebServerConfig {
    @Bean
    public ServletWebServerFactory undertowServletWebServerFactory() {
        return new UndertowServletWebServerFactory();
    }

    static class UndertowCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return true;
        }
    }
}
