package hello.testdriven.medium;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.testdriven.post.domain.PostUpdate;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("사용자는 게시물을 단건 조회 할 수 있다.")
    void getPostById() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("hello world"))
                .andExpect(jsonPath("$.createdAt").value("1678530673958"))
                .andExpect(jsonPath("$.writer.id").value(1));

    }

    @Test
    @DisplayName("사용자는 존재하지 않는 Post 아이디로 api 호출을 할 경우 404 응답을 받는다.")
    void getPostByIdException() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/posts/1233424"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Posts에서 ID 1233424를 찾을 수 없습니다."));
    }


    @Test
    @DisplayName("사용자는 Post 정보를 수정 할 수 있다.")
    void updatePost() throws Exception{
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("chang post content")
                .build();
        // when
        // then
        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("chang post content"));
    }
}