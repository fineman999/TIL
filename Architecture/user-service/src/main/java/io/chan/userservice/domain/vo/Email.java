package io.chan.userservice.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  validation를 체크하는 비즈 로직을 추가할 수 있다.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {
    private String value;

    public static Email create(String email) {
        validate(email);
        return new Email(email);
    }

    private static void validate(final String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }


}
