package tobyspring.config.autoconfig;

import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import tobyspring.config.ConditionalMyOnClass;
import tobyspring.config.MyAutoConfiguration;

@MyAutoConfiguration
@ConditionalMyOnClass("org.apache.catalina.startup.Tomcat")
public class TomcatWebServerConfig {

    @Bean
    @ConditionalOnMissingBean
    public ServletWebServerFactory tomcatServletWebServerFactory(ServerProperties properties) {
        TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();

        serverFactory.setContextPath(properties.getContextPath());
        serverFactory.setPort(properties.getPort());

        return serverFactory;
    }

    @Bean
    public ServerProperties serverProperties(Environment env) {
        ServerProperties properties = new ServerProperties();

        properties.setContextPath(env.getProperty("contextPath"));
        properties.setPort(Integer.parseInt(env.getProperty("port")));
        return properties;
    }
}
