package io.chan.springcoresecurity.domain.dto;

import io.chan.springcoresecurity.domain.entity.Account;
import io.chan.springcoresecurity.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String id;
    private String username;
    private String email;
    private int age;
    private String password;
    private List<String> roles;

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId().toString())
                .username(account.getUsername())
                .email(account.getEmail())
                .password(account.getPassword())
                .build();
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}


