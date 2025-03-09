package io.chan.queuingsystemforjava.support.controller;

import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 문서 테스트 코드 작성 시")
public class DocumentationTest extends BaseControllerTest {

    @Test
    @DisplayName("GET 요청을 다음과 같이 문서화 할 수 있다.")
    public void getDocs() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(get("/test/docs/hello").param("name", "ticket"));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                queryParameters(parameterWithName("name").description("이름")),
                                responseFields(
                                        fieldWithPath("hello")
                                                .type(JsonFieldType.STRING)
                                                .description("안녕, 이름"))));
    }

    @Test
    @DisplayName("POST 요청은 다음과 같이 문서화 할 수 있다.")
    void postDocs() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(new DocsController.HelloRequest("name"));

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/test/docs/hello/{test}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(parameterWithName("test").description("테스트용 경로 변수")),
                                requestFields(
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("이름")),
                                responseFields(
                                        fieldWithPath("pathVariable")
                                                .type(JsonFieldType.STRING)
                                                .description("경로 변수"),
                                        fieldWithPath("hello")
                                                .type(JsonFieldType.STRING)
                                                .description("안녕, 이름"))));
    }
}
