package io.chan.springcoresecurity.security.metadatasource;

import io.chan.springcoresecurity.security.service.SecurityResourceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    // 순서를 보장하는 LinkedHashMap 사용
    private final LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    private final SecurityResourceService securityResourceService;

    public UrlFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMatcherListLinkedHashMap, SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
        this.requestMap.putAll(requestMatcherListLinkedHashMap);
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        FilterInvocation fi = (FilterInvocation) object;

        HttpServletRequest request = fi.getRequest();



        for (RequestMatcher matcher : requestMap.keySet()) {
            if (matcher.matches(request)) {
                return requestMap.get(matcher);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        this.requestMap.values().forEach(allAttributes::addAll);
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public void reload() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = securityResourceService.getResourceList();
        requestMap.clear();
        requestMap.putAll(reloadedMap);
    }
}
