package io.chan.userservice.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 암호규칙을 점검하는 비즈 로직을 추가할 수 있다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {
    @Column(name = "password")
    private String value;

    public static Password create(String password) {
        return new Password(password);
    }
}
