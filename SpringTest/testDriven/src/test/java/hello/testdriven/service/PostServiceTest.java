package hello.testdriven.service;

import hello.testdriven.exception.ResourceNotFoundException;
import hello.testdriven.model.dto.PostCreateDto;
import hello.testdriven.model.dto.PostUpdateDto;
import hello.testdriven.repository.PostEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("getById는 존재하는 게시물을 내려준다.")
    void getById() {
        // given
        // when
        PostEntity result = postService.getById(1L);

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
            PostEntity result = postService.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("PostCreateDto를 이용하여 게시물을 생성할 수 있다.")
    void createByPostCreateDto() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .content("create Using by postUserDto")
                .writerId(1)
                .build();
        // when
        PostEntity result = postService.create(postCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("create Using by postUserDto");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
    }

    @Test
    @DisplayName("postUpdatedDto를 이용하여 게시물을 수정할 수 있다.")
    void updateByPostUpdateDto() {
        // given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("It's too difficult!")
                .build();

        // when
        postService.update(1, postUpdateDto);

        // then
        PostEntity postEntity = postService.getById(1);
        assertThat(postEntity.getContent()).isEqualTo("It's too difficult!");
        assertThat(postEntity.getModifiedAt()).isGreaterThan(0);

    }
}