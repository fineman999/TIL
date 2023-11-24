package io.chan.springcoresecurity.domain.dto;

import io.chan.springcoresecurity.domain.entity.Resources;
import io.chan.springcoresecurity.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesDto{

    private String id;
    private String resourceName;
    private String httpMethod;
    private int orderNum;
    private String resourceType;
    private String roleName;
    private Set<Role> roleSet;

    public static ResourcesDto from(Resources resources) {
        return ResourcesDto.builder()
                .id(resources.getId().toString())
                .resourceName(resources.getResourceName())
                .httpMethod(resources.getHttpMethod())
                .orderNum(resources.getOrderNum())
                .resourceType(resources.getResourceType())
                .roleSet(resources.getRoleSet())
                .roleName(resources.getRoleSet().iterator().next().getRoleName())
                .build();
    }
}
