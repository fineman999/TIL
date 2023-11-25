package io.chan.springcoresecurity.security.factory;

import io.chan.springcoresecurity.security.service.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private final LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    public UrlResourcesMapFactoryBean(SecurityResourceService securityResourceService) {
        this.resourceMap = securityResourceService.getResourceList();
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() {
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
