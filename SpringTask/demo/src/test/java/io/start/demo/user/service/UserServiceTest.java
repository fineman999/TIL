package io.start.demo.user.service;
import io.start.demo.common.domain.exception.CertificationCodeNotMatchedException;
import io.start.demo.common.domain.exception.ResourceNotFoundException;
import io.start.demo.mock.FakeMailSender;
import io.start.demo.mock.FakeUserRepository;
import io.start.demo.mock.TestClockHolder;
import io.start.demo.mock.TestUuidHolder;
import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserCreate;
import io.start.demo.user.domain.UserStatus;
import io.start.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("spring2@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("spring@naver.com")
                .nickname("spring")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    @DisplayName("getByEmail은 Active 상태인 유저를 찾을 수 있다")
    void getByEmailIsActive() {

        // given
        String email = "spring2@naver.com";

        // when
        User result = userService.getByEmail(email);

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
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById은 Active 상태인 유저를 찾을 수 있다")
    void getByIdIsActive() {
        // given
        long id = 1L;

        // when
        User result = userService.getById(id);

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
            User result = userService.getById(id);
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

        // when
        User result = userService.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
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
//        User user = userService.getByEmail("spring2@naver.com");
//        User result = userService.update(user.getId(), userUpdate);
        userService.update(1, userUpdate);
        // then
        User result = userService.getById(1);
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
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 인증 코드로 ACTIVE 시킬 수 있다.")
    void updatedPendingUserToActiveUserByVerifyCode() {

        // given
        // when
        userService.verifyEmail(2, "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
        // then
        User user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 잘못된 인증코드를 받으면 에러를 던진다.")
    void doseNotUpdatedPendingUserToActiveUserByVerifyCode() {

        // given
        // when
        // then
        assertThatThrownBy(() -> userService.verifyEmail(2, "aaaa-aaaa-aaaa-aaaaaaaaaaaa")).isInstanceOf(CertificationCodeNotMatchedException.class);

    }




}