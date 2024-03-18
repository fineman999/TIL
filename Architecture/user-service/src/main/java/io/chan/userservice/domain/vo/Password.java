package io.chan.userservice.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 암호규칙을 점검하는 비즈 로직을 추가할 수 있다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {
    private String value;

    public static Password create(String password) {
        return new Password(password);
    }
}
