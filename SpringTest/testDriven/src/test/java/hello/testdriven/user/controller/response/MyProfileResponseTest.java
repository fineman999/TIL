package hello.testdriven.user.controller.response;

import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MyProfileResponseTest {

    @Test
    @DisplayName("User로 응답을 생성할 수 있다.")
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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // then
        assertThat(myProfileResponse.getId()).isEqualTo(1);
        assertThat(myProfileResponse.getEmail()).isEqualTo("spring3@naver.com");
        assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);

    }


}