package io.chan.springcoresecurity.domain.entity;

import io.chan.springcoresecurity.domain.dto.AccountDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = "Id")
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long Id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(name = "account_roles",
            joinColumns = { @JoinColumn(name = "account_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> userRoles = new HashSet<>();

    @Builder
    public Account(Long id, String username, String password, String email, Set<Role> role) {
        Id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRoles = role;
    }

    public static Account from(AccountDto accountDto) {
        return Account.builder()
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .email(accountDto.getEmail())
                .build();
    }

    public static Account fromWithId(AccountDto accountDto) {
        return Account.builder()
                .id(Long.valueOf(accountDto.getId()))
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .email(accountDto.getEmail())
                .build();
    }

    public void setPassword(String encode) {
        this.password = encode;
    }

    public void setUserRoles(Set<Role> roles) {
        this.userRoles = roles;
    }
}
