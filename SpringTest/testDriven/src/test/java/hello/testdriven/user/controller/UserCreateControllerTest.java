package hello.testdriven.user.controller;


import hello.testdriven.mock.TestContainer;
import hello.testdriven.user.controller.response.UserResponse;
import hello.testdriven.user.domain.UserCreate;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {



    @Test
    @DisplayName("사용자는 회원가입을 할 수 있고 회원가입된 사용자는 PENDING 상태이다.")
    void createUser() throws Exception {

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
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(userCreate);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getEmail()).isEqualTo("spring3@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("hahaha");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }
}