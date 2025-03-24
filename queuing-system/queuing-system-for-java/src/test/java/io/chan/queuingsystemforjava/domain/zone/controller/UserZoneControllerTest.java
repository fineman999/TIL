package io.chan.queuingsystemforjava.domain.zone.controller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.zone.dto.ZoneElement;
import io.chan.queuingsystemforjava.domain.zone.service.UserZoneService;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserZoneController.class)
class UserZoneControllerTest extends BaseControllerTest {
    @MockitoBean
    private UserZoneService userZoneService;

    @Test
    @DisplayName("GET /api/v1/performances/{performanceId}/zones")
    void getZones() throws Exception {
        // given
        long performanceId = 1L;
        ZoneElement zoneElement = new ZoneElement(1L, "테스트 구역");

        given(userZoneService.getZones(performanceId))
                .willReturn(ItemResult.of(List.of(zoneElement)));

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/performances/{performanceId}/zones", performanceId)
                                .header(HttpHeaders.AUTHORIZATION, userBearerToken));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
                                pathParameters(
                                        parameterWithName("performanceId").description("공연 ID")),
                                responseFields(
                                        fieldWithPath("items").description("구역 목록"),
                                        fieldWithPath("items[].id").description("구역 ID"),
                                        fieldWithPath("items[].zoneName").description("구역 이름"))));
    }
}