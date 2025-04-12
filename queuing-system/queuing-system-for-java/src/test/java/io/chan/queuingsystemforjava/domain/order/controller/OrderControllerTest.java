package io.chan.queuingsystemforjava.domain.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.dto.OrderRequest;
import io.chan.queuingsystemforjava.domain.order.dto.OrderResponse;
import io.chan.queuingsystemforjava.domain.order.service.OrderService;
import io.chan.queuingsystemforjava.global.security.AuthenticationToken;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import java.math.BigDecimal;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest extends BaseControllerTest {

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private AuthenticationToken authenticationToken;

    public static final String PERFORMANCE_ID = "performanceId";

    @BeforeEach
    void setUp() {
        Mockito.when(authenticationToken.getPrincipal()).thenReturn(userMemberContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("주문 생성 API 호출 시")
    void createOrder() throws Exception {
        // Given
        OrderRequest request = new OrderRequest(
                "ORDER_perf123_seat456_16987654321",
                123L,
                456L,
                new BigDecimal("50000.00"),
                "customer@example.com",
                "김고객",
                "01012341234",
                "공연 티켓 - 좌석 A01"
        );

        OrderResponse response = OrderResponse.from(1L, request.orderId(), "PENDING");

        given(orderService.createOrder(any(OrderRequest.class), any(Member.class)))
                .willReturn(response);

        // When
        ResultActions result = mockMvc.perform(
                post("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                        .header(PERFORMANCE_ID, 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
        );

        // Then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                                        headerWithName(PERFORMANCE_ID).description("공연 ID")
                                ),
                                requestFields(
                                        fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 ID"),
                                        fieldWithPath("performanceId").type(JsonFieldType.NUMBER).description("공연 ID"),
                                        fieldWithPath("seatId").type(JsonFieldType.NUMBER).description("좌석 ID"),
                                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("결제 금액"),
                                        fieldWithPath("customerEmail").type(JsonFieldType.STRING).description("고객 이메일").optional(),
                                        fieldWithPath("customerName").type(JsonFieldType.STRING).description("고객 이름").optional(),
                                        fieldWithPath("customerMobilePhone").type(JsonFieldType.STRING).description("고객 전화번호").optional(),
                                        fieldWithPath("orderName").type(JsonFieldType.STRING).description("주문 이름")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 응답"),
                                        fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 ID"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("주문 상태")
                                )
                        )
                );
    }
}