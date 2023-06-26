package io.start.demo.user.controller;

import io.start.demo.common.domain.exception.CertificationCodeNotMatchedException;
import io.start.demo.common.domain.exception.ResourceNotFoundException;
import io.start.demo.mock.TestClockHolder;
import io.start.demo.mock.TestContainer;
import io.start.demo.user.controller.response.MyProfileResponse;
import io.start.demo.user.controller.response.UserResponse;
import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserStatus;
import io.start.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserControllerTest {

    @Test
    @DisplayName("사용자는 특정 유저의 주소가 소거된 정보를 전달 받을 수 있다.")
    void getUserById() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build());

        // when
        ResponseEntity<UserResponse> result = testContainer.userController
                .getById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("spring@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kok202");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    @DisplayName("사용자는 존재하지 않는 유저의 아이디로 api 호출을 할 경우 404 응답을 받는다.")
    void getUserByIdException() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        // then
        assertThatThrownBy(() -> testContainer.userController.getById(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("사용자는 인증 코드로 계정을 활성화 시킬 수 있다.")
    void verifyEmail() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build());
        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo((UserStatus.ACTIVE));
    }
    @Test
    @DisplayName("사용자는 인증 코드가 일치하지 않을 경우 권한 없음 에러를 내려준다.")
    void verifyEmailByException() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build());
        // when
        // then
        assertThatThrownBy(() -> testContainer.userController.verifyEmail(1, "aaaaaaa-aaaa-aaaa-"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }
    @Test
    @DisplayName("사용자는 내 정보를 불러올 때 개인정보인 주소도 갖고 올 수 있다.")
    void getMyInfo() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(1678530673958L))
                .build();
        testContainer.userRepository.save(User.builder()
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build());

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
                .getMyInfo("spring@naver.com");
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("spring@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("kok202");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1678530673958L);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");

    }

    @Test
    @DisplayName("사용자는 내 정보를 수정할 수 있다.")
    void updateMyInfo() {

        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build());

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController
                .updateMyInfo("spring@naver.com", UserUpdate.builder()
                        .address("Busan")
                        .nickname("Spring3")
                        .build());
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("spring@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("Spring3");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getAddress()).isEqualTo("Busan");

    }

}