package io.start.demo.user.controller;


import io.start.demo.common.domain.utils.ApiUtils;
import io.start.demo.mock.TestContainer;
import io.start.demo.user.controller.response.UserResponse;
import io.start.demo.user.domain.UserCreate;
import io.start.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {



    @Test
    @DisplayName("사용자는 회원가입을 할 수 있고 회원가입된 사용자는 PENDING 상태이다.")
    void createUser() {

        // given
        TestContainer testContainer = TestContainer.builder()
//                .uuidHolder(new TestUuidHolder("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .uuidHolder(()->"aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        UserCreate userCreate = UserCreate.builder()
                .email("spring3@naver.com")
                .nickname("hahaha")
                .address("busan")
                .build();

        // when
        ResponseEntity<ApiUtils.ApiResult<UserResponse>> result = testContainer.userCreateController.create(userCreate);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getResponse().getId()).isEqualTo(1);
        assertThat(result.getBody().getResponse().getEmail()).isEqualTo("spring3@naver.com");
        assertThat(result.getBody().getResponse().getNickname()).isEqualTo("hahaha");
        assertThat(result.getBody().getResponse().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getResponse().getLastLoginAt()).isNull();
        assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }
}