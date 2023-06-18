package hello.testdriven.post.domain;


import hello.testdriven.mock.TestClockHolder;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PostTest {

    @Test
    @DisplayName("PostCreate으로 게시물을 만들 수 있다.")
    void postCreate() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloWorld")
                .build();
        User writer = User.builder()
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        // when
        Post post = Post.from(writer, postCreate, new TestClockHolder(1679530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("helloWorld");
        assertThat(post.getCreatedAt()).isEqualTo(1679530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("spring3@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("spring3");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    @DisplayName("PostUpdate 게시물을 만들 수 있다.")
    void postUpdate() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("helloWorld")
                .build();
        User writer = User.builder()
                .email("spring3@naver.com")
                .nickname("spring3")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        Post post = Post.builder()
                .id(1L)
                .content("first Init")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(writer)
                .build();

        // when
        post = post.update(postUpdate, new TestClockHolder(1679530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("helloWorld");
        assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("spring3@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("spring3");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}