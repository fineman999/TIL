package io.chan.userservice.domain.vo;

import io.chan.userservice.domain.enums.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {
    private UserRole role;

    public static Authority create(UserRole role) {
        return new Authority(role);
    }

}
