package io.chan.queuingsystemforjava.domain.zone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.chan.queuingsystemforjava.domain.zone.dto.CreateZoneElement;
import io.chan.queuingsystemforjava.domain.zone.dto.CreateZoneRequest;
import io.chan.queuingsystemforjava.domain.zone.service.AdminZoneService;
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

@WebMvcTest(AdminZoneController.class)
public class AdminZoneControllerTest extends BaseControllerTest {

    @MockitoBean
    private AdminZoneService service;


    @Test
    @DisplayName("POST /api/v1/performances/{performanceId}/zones")
    void createZones() throws Exception {
        // given
        long performanceId = 1L;
        String content = createBodyContent();

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/performances/{performanceId}/zones", performanceId)
                                .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

        // then
        result.andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION)
                                                .description("어드민 액세스 토큰")),
                                pathParameters(
                                        parameterWithName("performanceId").description("공연 ID")),
                                requestFields(
                                        fieldWithPath("zones[].zoneName")
                                                .type(JsonFieldType.STRING)
                                                .description("존 이름"))));
    }

    private String createBodyContent() throws JsonProcessingException {
        CreateZoneElement createZoneElement = new CreateZoneElement("VIP");
        CreateZoneElement createZoneElement2 = new CreateZoneElement("General");

        CreateZoneRequest createZoneRequest = new CreateZoneRequest(List.of(createZoneElement, createZoneElement2));

        return objectMapper.writeValueAsString(createZoneRequest);
    }
}