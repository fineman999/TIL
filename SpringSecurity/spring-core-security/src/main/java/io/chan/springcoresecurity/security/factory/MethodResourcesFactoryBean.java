package io.chan.springcoresecurity.security.factory;

import io.chan.springcoresecurity.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
public class MethodResourcesFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private final LinkedHashMap<String, List<ConfigAttribute>> resourceMap;

    public MethodResourcesFactoryBean(SecurityResourceService securityResourceService) {
        this.resourceMap = securityResourceService.getMethodResourceList();
    }

    @Override
    public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
        return resourceMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true; // 싱글톤으로 사용할 것이므로 true
    }
}
