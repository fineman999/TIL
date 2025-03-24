package io.chan.queuingsystemforjava.domain.seat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatCreationElement;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatCreationRequest;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatGradeCreationElement;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatGradeCreationRequest;
import io.chan.queuingsystemforjava.domain.seat.service.AdminSeatService;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminSeatController.class)
public class AdminSeatControllerTest extends BaseControllerTest {

    @MockitoBean
    private AdminSeatService adminSeatService;

    @Test
    @DisplayName("관리자 좌석 생성 API")
    void createSeats() throws Exception {
        // given
        long performanceId = 1L;
        long zoneId = 2L;
        String content = createBodyContent();

        // when
        ResultActions result =
                mockMvc.perform(
                        post(
                                        "/api/v1/performances/{performanceId}/zones/{zoneId}/seats",
                                        performanceId,
                                        zoneId)
                                .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        result.andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
                                pathParameters(
                                        parameterWithName("performanceId").description("공연 ID"),
                                        parameterWithName("zoneId").description("존 ID")),
                                requestFields(
                                        fieldWithPath("seatCreationElements[].seatCode")
                                                .type(JsonFieldType.STRING)
                                                .description("좌석 코드"),
                                        fieldWithPath("seatCreationElements[].seatGradeId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("좌석 등급 id"))));
    }

    private String createBodyContent() throws JsonProcessingException {
        SeatCreationElement seat1 = new SeatCreationElement("A01", 1L);
        SeatCreationElement seat2 = new SeatCreationElement("B01", 2L);
        SeatCreationRequest seatCreationRequest = new SeatCreationRequest(List.of(seat1, seat2));

        return objectMapper.writeValueAsString(seatCreationRequest);
    }

    @Test
    @DisplayName("POST /api/v1/performances/{performanceId}/grades 요청")
    void createSeatGrades() throws Exception {
        // given
        long performanceId = 1L;
        String content = makeRequest();

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/performances/{performanceId}/grades", performanceId)
                                .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        result.andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
                                pathParameters(
                                        parameterWithName("performanceId").description("공연 ID")),
                                requestFields(
                                        fieldWithPath("seatGradeCreationElements[].price")
                                                .type(JsonFieldType.NUMBER)
                                                .description("등급 가격"),
                                        fieldWithPath("seatGradeCreationElements[].seatGradeName")
                                                .type(JsonFieldType.STRING)
                                                .description("좌석 등급명"))));
    }

    private String makeRequest() throws JsonProcessingException {

        SeatGradeCreationElement seatGrade1 = new SeatGradeCreationElement(10000L, "Grade1");
        SeatGradeCreationElement seatGrade2 = new SeatGradeCreationElement(20000L, "Grade2");

        SeatGradeCreationRequest request = new SeatGradeCreationRequest(List.of(seatGrade1, seatGrade2));

        return objectMapper.writeValueAsString(request);
    }
}