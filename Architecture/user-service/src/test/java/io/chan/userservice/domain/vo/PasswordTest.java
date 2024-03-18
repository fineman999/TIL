package io.chan.userservice.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

    @DisplayName("Password 객체 생성 테스트")
    @Test
    void create() {
        Password password = Password.create("password");
        assertThat(password.getValue()).isEqualTo("password");
    }
}