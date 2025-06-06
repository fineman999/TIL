package io.chan.queuingsystemforjava.domain.ticket.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.chan.queuingsystemforjava.domain.ticket.dto.TicketPaymentResponse;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationService;
import io.chan.queuingsystemforjava.global.security.AuthenticationToken;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest extends BaseControllerTest {

  @MockitoBean(name = "persistencePessimisticReservationService")
  private ReservationService reservationService;

  @MockitoBean private SecurityContext securityContext;

  @MockitoBean private AuthenticationToken authenticationToken;

  public static final String PERFORMANCE_ID = "performanceId";

  @BeforeEach
  void setUp() {
    Mockito.when(authenticationToken.getPrincipal()).thenReturn(userMemberContext);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authenticationToken);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  @DisplayName("자리 선택 API 호출 시")
  void selectSeat() throws Exception {
    // given
    SeatSelectionRequest request = new SeatSelectionRequest(1L);

    // when
    ResultActions result =
        mockMvc.perform(
            post("/api/v1/seats/select")
                .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                .header(PERFORMANCE_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

    // then
    result
        .andExpect(status().isOk())
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                    headerWithName(PERFORMANCE_ID).description("공연 ID")),
                requestFields(
                    fieldWithPath("seatId").type(JsonFieldType.NUMBER).description("좌석 ID"))));
  }

  @Test
  @DisplayName("티켓 결제 API 호출 시")
  void reservationTicket() throws Exception {
    String orderId = "1234567890123456";
    BigDecimal price = new BigDecimal("100.00");
    // given
    TicketPaymentRequest request = new TicketPaymentRequest(1L, "1234567890123456", orderId, price);

    TicketPaymentResponse response = TicketPaymentResponse.create(1L);

    given(reservationService.reservationTicket(any(String.class), any(TicketPaymentRequest.class)))
        .willReturn(response);


    // when
    ResultActions result =
        mockMvc.perform(
            post("/api/v1/tickets")
                .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                .header(PERFORMANCE_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

    // then
    result
        .andExpect(status().isOk())
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                    headerWithName(PERFORMANCE_ID).description("공연 ID")),
                requestFields(
                    fieldWithPath("seatId").type(JsonFieldType.NUMBER).description("좌석 ID"),
                    fieldWithPath("paymentKey").type(JsonFieldType.STRING).description("결제 키"),
                    fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 ID"),
                    fieldWithPath("amount").type(JsonFieldType.NUMBER).description("결제 금액"))));
  }

  @Test
  @DisplayName("좌석 점유 해제 API")
  void releaseSeat() throws Exception {
    // given
    SeatSelectionRequest request = new SeatSelectionRequest(1L);

    // when
    ResultActions result =
        mockMvc.perform(
            post("/api/v1/seats/release")
                .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                .header(PERFORMANCE_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

    // then
    result
        .andExpect(status().isOk())
        .andDo(
            restDocs.document(
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                    headerWithName(PERFORMANCE_ID).description("공연 ID")),
                requestFields(
                    fieldWithPath("seatId").type(JsonFieldType.NUMBER).description("좌석 ID"))));
  }
}
