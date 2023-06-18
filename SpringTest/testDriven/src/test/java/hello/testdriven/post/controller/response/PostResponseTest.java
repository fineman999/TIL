package hello.testdriven.post.controller.response;

import hello.testdriven.post.domain.Post;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class PostResponseTest {

    @Test
    @DisplayName("Post로 응답을 생성할 수 있다.")
    void from() {
        // given
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .email("spring3@naver.com")
                        .nickname("spring3")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build())
                .build();
        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getContent()).isEqualTo("helloworld");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("spring3@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("spring3");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);

    }
}