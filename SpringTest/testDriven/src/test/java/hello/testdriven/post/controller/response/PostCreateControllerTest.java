package hello.testdriven.post.controller.response;
import hello.testdriven.mock.TestContainer;
import hello.testdriven.post.domain.PostCreate;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


class PostCreateControllerTest {


    @Test
    @DisplayName("사용자는 게시판을 만들 수 있다.")
    void createPost() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678530673958L)
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

        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("Hello!!")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.create(postCreate);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getContent()).isEqualTo("Hello!!");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("kok202");
    }
}