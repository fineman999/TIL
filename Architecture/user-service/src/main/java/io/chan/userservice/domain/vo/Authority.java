package io.chan.userservice.domain.vo;

import io.chan.userservice.domain.enums.UserRole;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Authority {
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static Authority create(UserRole role) {
        return new Authority(role);
    }

}
