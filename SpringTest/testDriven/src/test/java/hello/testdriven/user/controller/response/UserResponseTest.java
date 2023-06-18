package hello.testdriven.user.controller.response;

import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserResponseTest {

    @Test
    @DisplayName("User으로 응답을 생성할 수 있다.")
    void from() {
        // given
        User user = User.builder()
                .id(1L)
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        // when
        UserResponse userResponse = UserResponse.from(user);

        // then
        assertThat(userResponse.getId()).isEqualTo(1);
        assertThat(userResponse.getEmail()).isEqualTo("spring3@naver.com");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);

    }
}