package hello.testdriven.service;

import hello.testdriven.common.domain.exception.CertificationCodeNotMatchedException;
import hello.testdriven.common.domain.exception.ResourceNotFoundException;
import hello.testdriven.user.domain.UserStatus;
import hello.testdriven.user.domain.UserCreate;
import hello.testdriven.user.domain.UserUpdate;
import hello.testdriven.user.infrastructure.UserEntity;
import hello.testdriven.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    @DisplayName("getByEmail은 Active 상태인 유저를 찾을 수 있다")
    void getByEmailIsActive() {

        // given
        String email = "spring2@naver.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }

    @Test
    @DisplayName("getByEmail은 Pending 상태인 유저를 찾을 수 없다")
    void doseNotGetByEmailIsPending() {

        // given
        String email = "spring@naver.com";

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById은 Active 상태인 유저를 찾을 수 있다")
    void getByIdIsActive() {
        // given
        long id = 1L;

        // when
        UserEntity result = userService.getById(id);

        // then
        assertThat(result.getNickname()).isEqualTo("kok202");
    }

    @Test
    @DisplayName("getById는 Pending 상태인 유저를 찾을 수 없다")
    void doseNotGetByIdIsPending() {
        // given
        long id = 2L;

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(id);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("userCreateDto를 이용하여 유저를 생성할 수 있다.")
    void createUserByUserCreateDto() {

        // given
        UserCreate userCreate = UserCreate.builder()
                .email("spring@naver.com")
                .address("spring")
                .nickname("Seoul")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo(T.T);
    }

    @Test
    @DisplayName("userUpdateDto를 이용하여 유저를 수정할 수 있다.")
    void updatedUserByUserUpdateDto() {

        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("spring3")
                .address("address3")
                .build();

        // when
//        UserEntity userEntity = userService.getByEmail("spring2@naver.com");
//        UserEntity result = userService.update(userEntity.getId(), userUpdate);
        userService.update(1, userUpdate);
        // then
        UserEntity result = userService.getById(1);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("spring3");
        assertThat(result.getAddress()).isEqualTo("address3");
    }

    @Test
    @DisplayName("user를 로그인 시키면 마지막 로그인 시간이 변경된다.")
    void updatedTimesByLogin() {

        // given
        // when
        userService.login(1);
        // then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
//        assertThat(userEntity.getLastLoginAt()).isEqualTo(T.T);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 인증 코드로 ACTIVE 시킬 수 있다.")
    void updatedPendingUserToActiveUserByVerifyCode() {

        // given
        // when
        userService.verifyEmail(2, "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
        // then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 잘못된 인증코드를 받으면 에러를 던진다.")
    void doseNotUpdatedPendingUserToActiveUserByVerifyCode() {

        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }




}