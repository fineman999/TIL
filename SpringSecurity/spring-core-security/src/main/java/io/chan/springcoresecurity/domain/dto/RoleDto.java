
package io.chan.springcoresecurity.domain.dto;

import io.chan.springcoresecurity.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto{

    private String id;
    private String roleName;
    private String roleDesc;

    public static RoleDto from(Role role) {
        return RoleDto.builder()
                .id(role.getId().toString())
                .roleName(role.getRoleName())
                .roleDesc(role.getRoleDesc())
                .build();
    }
}


