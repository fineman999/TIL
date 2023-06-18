package hello.testdriven.post.controller.response;

import hello.testdriven.common.domain.exception.ResourceNotFoundException;
import hello.testdriven.mock.TestContainer;
import hello.testdriven.post.domain.Post;
import hello.testdriven.post.domain.PostUpdate;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PostControllerTest {

    @Test
    @DisplayName("사용자는 게시물을 단건 조회 할 수 있다.")
    void getPostById() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        testContainer.postRepository.save(Post.builder()
                .writer(user)
                .content("testing....")
                .createdAt(100L)
                .build());
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getContent()).isEqualTo("testing....");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("spring@naver.com");

    }

    @Test
    @DisplayName("사용자는 존재하지 않는 Post 아이디로 api 호출을 할 경우 404 응답을 받는다.")
    void getPostByIdException() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        // when
        // then
        assertThatThrownBy(() -> testContainer.postController.getById(1))
                .isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    @DisplayName("사용자는 Post 정보를 수정 할 수 있다.")
    void updatePost() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678530673958L)
                .build();
        User user = User.builder()
                .email("spring@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        testContainer.postRepository.save(Post.builder()
                .writer(user)
                .content("testing....")
                .createdAt(100L)
                .build());

        PostUpdate postUpdate = PostUpdate.builder()
                .content("chang post content")
                .build();
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.update(1L, postUpdate);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(1678530673958L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getContent()).isEqualTo("chang post content");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("spring@naver.com");

    }
}