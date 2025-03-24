package io.chan.queuingsystemforjava.domain.performance.controller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.performance.conroller.UserPerformanceController;
import io.chan.queuingsystemforjava.domain.performance.dto.PerformanceElement;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserPerformanceController.class)
class UserPerformanceControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("GET /api/v1/performances")
    void getPerformances() throws Exception {
        // given
        PerformanceElement performanceElement =
                new PerformanceElement(1L, "테스트 공연", "테스트 장소", ZonedDateTime.now());

        given(userPerformanceService.getPerformances())
                .willReturn(ItemResult.of(List.of(performanceElement)));

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/performances").header(HttpHeaders.AUTHORIZATION, userBearerToken));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("items").description("공연 목록"),
                                        fieldWithPath("items[].performanceId").description("공연 ID"),
                                        fieldWithPath("items[].performanceName")
                                                .description("공연 이름"),
                                        fieldWithPath("items[].performancePlace")
                                                .description("공연 장소"),
                                        fieldWithPath("items[].performanceTime")
                                                .description("공연 시간"))));
    }

    @Test
    @DisplayName("공연 상세 정보 API")
    void getPerformance() throws Exception {
        // given
        Long performanceId = 1L;
        PerformanceElement performanceElement =
                new PerformanceElement(performanceId, "테스트 공연", "테스트 장소", ZonedDateTime.now());

        given(userPerformanceService.getPerformance(performanceId)).willReturn(performanceElement);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/performances/{performanceId}", performanceId)
                                .header(HttpHeaders.AUTHORIZATION, userBearerToken));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("performanceId")
                                                .description("공연 ID")
                                                .type(JsonFieldType.NUMBER),
                                        fieldWithPath("performanceName")
                                                .description("공연 이름")
                                                .type(JsonFieldType.STRING),
                                        fieldWithPath("performancePlace")
                                                .description("공연 장소")
                                                .type(JsonFieldType.STRING),
                                        fieldWithPath("performanceTime")
                                                .description("공연 시간")
                                                .type(JsonFieldType.STRING))));
    }
}