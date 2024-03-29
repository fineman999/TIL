package io.chan.springcoresecurity.security.service;

import io.chan.springcoresecurity.domain.entity.AccessIp;
import io.chan.springcoresecurity.domain.entity.Resources;
import io.chan.springcoresecurity.repository.AccessIpRepository;
import io.chan.springcoresecurity.repository.ResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@RequiredArgsConstructor
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;
    private final AccessIpRepository accessIpRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resources = resourcesRepository.findAllResources();
        resources.forEach(re -> {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            re.getRoleSet().forEach(role ->
                    configAttributes.add(new SecurityConfig(role.getRoleName())));
            result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributes);
        });
        return result;
    }

    public LinkedHashMap<String, List<ConfigAttribute>> getMethodResourceList() {
        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resources = resourcesRepository.findAllMethodResources();
        return getResources(resources, result);
    }

    public List<AccessIp> getAccessIps() {
        return accessIpRepository.findAll();
    }

    public LinkedHashMap<String, List<ConfigAttribute>> getPointcutResourceList() {
        LinkedHashMap<String, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resources = resourcesRepository.findAllPointcutResources();
        return getResources(resources, result);
    }

    private LinkedHashMap<String, List<ConfigAttribute>> getResources(List<Resources> resources, LinkedHashMap<String, List<ConfigAttribute>> result) {
        resources.forEach(re -> {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            re.getRoleSet().forEach(role ->
                    configAttributes.add(new SecurityConfig(role.getRoleName())));
            result.put(re.getResourceName(), configAttributes);
        });
        return result;
    }
}
