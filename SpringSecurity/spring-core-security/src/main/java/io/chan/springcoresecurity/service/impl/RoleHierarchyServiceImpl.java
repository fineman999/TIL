package io.chan.springcoresecurity.service.impl;

import io.chan.springcoresecurity.domain.entity.RoleHierarchy;
import io.chan.springcoresecurity.repository.RoleHierarchyRepository;
import io.chan.springcoresecurity.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService {
    private final RoleHierarchyRepository roleHierarchyRepository;
    @Override
    public String findAllHierarchy() {
        List<RoleHierarchy> roleHierarchies = roleHierarchyRepository.findAll();

        StringBuilder stringBuilder = new StringBuilder();

        roleHierarchies.forEach(roleHierarchy -> {
            if (roleHierarchy.getParentName() != null) {
                stringBuilder.append(roleHierarchy.getParentName().getChildName())
                        .append(" > ")
                        .append(roleHierarchy.getChildName())
                        .append("\n");
            }
        });

        return stringBuilder.toString();
    }
}
