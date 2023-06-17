package hello.testdriven.user.infrastructure;

import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql(scripts = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByIdAndStatus 유저 데이터를 찾아올 수 있다.")
    void findUserDataUsingFindByIdAndStatus() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByIdAndStatus 유저 데이터가 없으면 Optional empty 를 내려준다.")
    void notFindUserDataUsingFindByIdAndStatus() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("findByEmailAndStatus 유저 데이터를 찾아올 수 있다.")
    void findUserDataUsingFindByEmailAndStatus() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("spring@naver.com", UserStatus.ACTIVE);

        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByEmailAndStatus 유저 데이터가 없으면 Optional empty 를 내려준다.")
    void notFindUserEmailUsingFindByIdAndStatus() {
        // given
        // when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("spring@naver.com", UserStatus.PENDING);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

   /* @Test
    @DisplayName("UserRepository가 제대로 연결되었다.")
    void completelyConnectedUserRepository() {
        // given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@naver.com");
        userEntity.setNickname("chan");
        userEntity.setAddress("Seoul");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("bbbb-aabbaa-aaaabb-aaaaabbbaaaaa");
        // when
        UserEntity result = userRepository.save(userEntity);

        // then
        assertThat(result.getId()).isNotNull();
    }*/

}