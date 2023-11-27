package io.chan.springcoresecurity.security.config;

import io.chan.springcoresecurity.security.factory.MethodResourcesFactoryBean;
import io.chan.springcoresecurity.security.processor.ProtectPointcutPostProcessor;
import io.chan.springcoresecurity.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import java.util.Objects;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final SecurityResourceService securityResourceService;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
        return new MapBasedMethodSecurityMetadataSource(Objects.requireNonNull(methodResourceFactoryBean().getObject()));
    }

    @Bean
    public MethodResourcesFactoryBean methodResourceFactoryBean() {
        return new MethodResourcesFactoryBean(securityResourceService, "method");
    }

    @Bean
    public MethodResourcesFactoryBean pointcutResourceFactoryBean() {
        return new MethodResourcesFactoryBean(securityResourceService, "pointcut");
    }

    @Bean
    public ProtectPointcutPostProcessor protectPointcutPostProcessor() {

        ProtectPointcutPostProcessor protectPointcutPostProcessor = new ProtectPointcutPostProcessor(mapBasedMethodSecurityMetadataSource());
        protectPointcutPostProcessor.setPointcutMap(pointcutResourceFactoryBean().getObject());

        return protectPointcutPostProcessor;
    }

//    @Bean
//    BeanPostProcessor protectPointcutPostProcessor() throws Exception {
//        Class<?> clazz = Class.forName("org.springframework.security.config.method.ProtectPointcutPostProcessor");
//        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(MapBasedMethodSecurityMetadataSource.class);
//        declaredConstructor.setAccessible(true);
//        Object instance = declaredConstructor.newInstance(mapBasedMethodSecurityMetadataSource());
//        Method setPointcutMap = instance.getClass().getMethod("setPointcutMap", Map.class);
//        setPointcutMap.setAccessible(true);
//        setPointcutMap.invoke(instance, Objects.requireNonNull(pointcutResourceFactoryBean().getObject()));
//
//        return (BeanPostProcessor) instance;
//    }
}
