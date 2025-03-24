package io.chan.queuingsystemforjava.domain.performance.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.chan.queuingsystemforjava.domain.performance.conroller.AdminPerformanceController;
import io.chan.queuingsystemforjava.domain.performance.dto.CreatePerformanceRequest;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminPerformanceController.class)
class AdminPerformanceControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("POST /api/v1/performances")
    void createPerformance() throws Exception {
        // given
        String content = createBodyContent();

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/performances")
                                .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        result.andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
                                requestFields(
                                        fieldWithPath("performanceName")
                                                .type(JsonFieldType.STRING)
                                                .description("공연 이름"),
                                        fieldWithPath("performanceLocation")
                                                .type(JsonFieldType.STRING)
                                                .description("공연 장소"),
                                        fieldWithPath("performanceShowtime")
                                                .type(JsonFieldType.STRING)
                                                .description("공연 시간"))));
    }

    private String createBodyContent() throws JsonProcessingException {
        CreatePerformanceRequest pcr = new CreatePerformanceRequest("공연 이름", "공연 장소", ZonedDateTime.now());
        return objectMapper.writeValueAsString(pcr);
    }
}