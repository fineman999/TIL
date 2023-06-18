package hello.testdriven.post.service;

import hello.testdriven.common.domain.exception.ResourceNotFoundException;
import hello.testdriven.mock.*;
import hello.testdriven.post.domain.Post;
import hello.testdriven.post.domain.PostCreate;
import hello.testdriven.post.domain.PostUpdate;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PostServiceTest {

    private PostServiceImpl postService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakePostRepository fakePostRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                        .postRepository(fakePostRepository)
                        .userRepository(fakeUserRepository)
                        .clockHolder(new TestClockHolder(1679530673958L))
                        .build();
        User user = User.builder()
                .id(1L)
                .email("spring2@naver.com")
                .nickname("kok202")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();
        fakeUserRepository.save(user);
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("spring@naver.com")
                .nickname("spring")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
        fakePostRepository.save(Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(user)
                .build());

    }

    @Test
    @DisplayName("getById는 존재하는 게시물을 내려준다.")
    void getById() {
        // given
        // when

            Post result = postService.getById(1L);

        // then
        assertThat(result.getContent()).isEqualTo("hello world");
        assertThat(result.getWriter().getEmail()).isEqualTo("spring2@naver.com");

    }

    @Test
    @DisplayName("getById는 존재하는 게시물을 찾을 수 없다.")
    void doseNotGetById() {

        // given
        // when
        // then
        assertThatThrownBy(() -> {
            Post result = postService.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("PostCreateDto를 이용하여 게시물을 생성할 수 있다.")
    void createByPostCreateDto() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .content("create Using by postUserDto")
                .writerId(1)
                .build();
        // when
        Post result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("create Using by postUserDto");
        assertThat(result.getCreatedAt()).isEqualTo(1679530673958L);
    }

    @Test
    @DisplayName("postUpdatedDto를 이용하여 게시물을 수정할 수 있다.")
    void updateByPostUpdateDto() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("It's too difficult!")
                .build();

        // when
        postService.update(1, postUpdate);

        // then
        Post post = postService.getById(1);
        assertThat(post.getContent()).isEqualTo("It's too difficult!");
        assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);

    }
}