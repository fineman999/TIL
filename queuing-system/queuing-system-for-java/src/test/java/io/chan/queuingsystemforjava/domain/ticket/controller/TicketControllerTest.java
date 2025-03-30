package io.chan.queuingsystemforjava.domain.ticket.controller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.performance.dto.PerformanceElement;
import io.chan.queuingsystemforjava.domain.seat.dto.response.SeatGradeElement;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketElement;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketSeatDetail;
import io.chan.queuingsystemforjava.domain.ticket.service.TicketService;
import io.chan.queuingsystemforjava.global.security.AuthenticationToken;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest extends BaseControllerTest {

    @MockitoBean
    private TicketService ticketService;
    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private AuthenticationToken authenticationToken;

    @BeforeEach
    void setUp() {
        Mockito.when(authenticationToken.getPrincipal()).thenReturn(userMemberContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("티켓 조회 API 호출 시")
    void selectMyTickets() throws Exception {
        // given
        PerformanceElement performanceElement =
                new PerformanceElement(1L, "흠뻑쇼", "서울", ZonedDateTime.now());
        SeatGradeElement seatGradeElement = new SeatGradeElement(1L, "VIP", 160000L);
        TicketSeatDetail seatElement = new TicketSeatDetail(2L, "A01", seatGradeElement);
        TicketElement ticketElement =
                new TicketElement(UUID.randomUUID(), performanceElement, seatElement);
        ItemResult<TicketElement> response = new ItemResult<>(List.of(ticketElement));

        given(ticketService.selectMyTicket(any())).willReturn(response);

        // when
        ResultActions result =
                mockMvc.perform(
                        get("/api/v1/members/tickets").header(HttpHeaders.AUTHORIZATION, userBearerToken));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")),
                                responseFields(
                                        fieldWithPath("items")
                                                .type(JsonFieldType.ARRAY)
                                                .description("티켓 목록"),
                                        fieldWithPath("items[].serialNumber")
                                                .type(JsonFieldType.STRING)
                                                .description("티켓 번호"),
                                        fieldWithPath("items[].seat")
                                                .type(JsonFieldType.OBJECT)
                                                .description("좌석 정보"),
                                        fieldWithPath("items[].seat.seatId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("좌석 ID"),
                                        fieldWithPath("items[].seat.seatCode")
                                                .type(JsonFieldType.STRING)
                                                .description("좌석 코드"),
                                        fieldWithPath("items[].seat.grade")
                                                .type(JsonFieldType.OBJECT)
                                                .description("좌석 등급"),
                                        fieldWithPath("items[].seat.grade.seatGradeId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("좌석 등급 ID"),
                                        fieldWithPath("items[].seat.grade.gradeName")
                                                .type(JsonFieldType.STRING)
                                                .description("좌석 등급 명"),
                                        fieldWithPath("items[].seat.grade.price")
                                                .type(JsonFieldType.NUMBER)
                                                .description("좌석 가격"),
                                        fieldWithPath("items[].performance.performanceId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("공연 ID"),
                                        fieldWithPath("items[].performance.performanceName")
                                                .type(JsonFieldType.STRING)
                                                .description("공연 이름"),
                                        fieldWithPath("items[].performance.performancePlace")
                                                .type(JsonFieldType.STRING)
                                                .description("공연 장소"),
                                        fieldWithPath("items[].performance.performanceTime")
                                                .type(JsonFieldType.STRING)
                                                .description("공연 시간"))));
    }
}