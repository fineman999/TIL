package hello.testdriven.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.testdriven.user.domain.UserStatus;
import hello.testdriven.user.domain.UserUpdate;
import hello.testdriven.user.infrastructure.UserEntity;
import hello.testdriven.user.infrastructure.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("사용자는 특정 유저의 주소가 소거된 정보를 전달 받을 수 있다.")
    void getUserById() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("spring2@naver.com"))
                .andExpect(jsonPath("$.nickname").value("kok202"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("사용자는 존재하지 않는 유저의 아이디로 api 호출을 할 경우 404 응답을 받는다.")
    void getUserByIdException() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/1233424"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 1233424를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("사용자는 인증 코드로 계정을 활성화 시킬 수 있다.")
    void verifyEmail() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                get("/api/users/{id}/verify", 2)
                .queryParam("certificationCode", "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(status().isFound());
        UserEntity userEntity = userRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
    @Test
    @DisplayName("사용자는 인증 코드가 일치하지 않을 경우 권한 없음 에러를 내려준다.")
    void verifyEmailByException() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                        get("/api/users/{id}/verify", 2)
                                .queryParam("certificationCode", "aaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("자격 증명에 실패하였습니다."));
    }
    @Test
    @DisplayName("사용자는 내 정보를 불러올 때 개인정보인 주소도 갖고 올 수 있다.")
    void getMyInfo() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/me").header("EMAIL", "spring2@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("spring2@naver.com"))
                .andExpect(jsonPath("$.nickname").value("kok202"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    @DisplayName("사용자는 내 정보를 수정할 수 있다.")
    void updateMyInfo() throws Exception {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("spring3")
                .address("Busan")
                .build();
        // when
        // then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "spring2@naver.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("spring2@naver.com"))
                .andExpect(jsonPath("$.nickname").value("spring3"))
                .andExpect(jsonPath("$.address").value("Busan"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

}