package io.start.demo.user.domain;

import io.start.demo.common.domain.exception.CertificationCodeNotMatchedException;
import io.start.demo.mock.TestClockHolder;
import io.start.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserTest {

    @Test
    @DisplayName("User는 UserCreate 객체로 생성할 수 있다.")
    void from() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .build();
        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("spring3@naver.com");
        assertThat(user.getNickname()).isEqualTo("spring3");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }

    @Test
    @DisplayName("User는 UserUpdate 객체로 수정할 수 있다.")
    void update() {

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

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("spring4")
                .address("Busan")
                .build();
        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("spring3@naver.com");
        assertThat(user.getNickname()).isEqualTo("spring4");
        assertThat(user.getAddress()).isEqualTo("Busan");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");


    }

    @Test
    @DisplayName("User는 로그인을 할 수 있고 로그인 시 마지막 로그인 시간이 변경된다.")
    void login() {

        // given
        User user = User.builder()
                .id(1L)
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        user = user.login(new TestClockHolder(1678530673958L));

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("spring3@naver.com");
        assertThat(user.getNickname()).isEqualTo("spring3");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    @DisplayName("User는 인증 코드로 계정을 활성화 할 수 있다.")
    void certification() {
        // given
        User user = User.builder()
                .id(1L)
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        user = user.certificate("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa", user.getCertificationCode());

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("spring3@naver.com");
        assertThat(user.getNickname()).isEqualTo("spring3");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    @DisplayName("User는 잘못된 인증 코드로 예외가 발생한다.")
    void certificationException() {
        // given
        User user = User.builder()
                .id(1L)
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        // then
        assertThatThrownBy(()->
                user.certificate("aaaaaaa-aaaa-aaaa-aaabbbb", user.getCertificationCode()))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}