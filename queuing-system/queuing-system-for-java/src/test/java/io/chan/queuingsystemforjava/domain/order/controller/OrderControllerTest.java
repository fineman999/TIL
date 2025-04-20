package io.chan.queuingsystemforjava.domain.order.controller;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.OrderStatus;
import io.chan.queuingsystemforjava.domain.order.dto.*;
import io.chan.queuingsystemforjava.domain.order.service.OrderCreationService;
import io.chan.queuingsystemforjava.domain.order.service.OrderQueryService;
import io.chan.queuingsystemforjava.global.security.AuthenticationToken;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest extends BaseControllerTest {

    @MockitoBean
    private OrderCreationService orderCreationService;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @MockitoBean
    private SecurityContext securityContext;

    @MockitoBean
    private AuthenticationToken authenticationToken;

    public static final String PERFORMANCE_ID = "performanceId";

    @BeforeEach
    void setUp() {
        // SecurityContext 및 AuthenticationToken 모킹
        given(authenticationToken.getPrincipal()).willReturn(userMemberContext);
        given(securityContext.getAuthentication()).willReturn(authenticationToken);
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
                "customer@example.com",
                "김고객",
                "01012341234",
                "공연 티켓 - 좌석 A01"
        );

        OrderResponse response = OrderResponse.from(1L, request.orderId(), "PENDING");

        given(orderCreationService.createOrder(any(OrderRequest.class), any(Member.class)))
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

    @Test
    @DisplayName("주문 목록 조회 API 호출 시")
    void getOrders() throws Exception {
        // Given
        OrderSearchRequest searchRequest = new OrderSearchRequest(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                "테스트 공연",
                OrderStatus.COMPLETED
        );

        OrderListResponse orderListResponse = new OrderListResponse(
                "ORDER123",
                "테스트 공연 - A-12",
                "테스트 공연",
                "테스트 장소",
                ZonedDateTime.now().plusDays(1),
                new BigDecimal("100.00"),
                "COMPLETED",
                ZonedDateTime.now()
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OrderListResponse> orderPage = new PageImpl<>(List.of(orderListResponse), pageable, 1);

        given(orderQueryService.getOrders(any(Member.class), any(OrderSearchRequest.class), any(Pageable.class)))
                .willReturn(orderPage);

        // When
        ResultActions result = mockMvc.perform(
                get("/api/v1/orders")
                        .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                        .param("startDate", searchRequest.startDate().toString())
                        .param("endDate", searchRequest.endDate().toString())
                        .param("orderName", searchRequest.orderName())
                        .param("status", searchRequest.status().name())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                                ),
                                queryParameters(
                                        parameterWithName("startDate").description("조회 시작 날짜 (yyyy-MM-dd)").optional(),
                                        parameterWithName("endDate").description("조회 종료 날짜 (yyyy-MM-dd)").optional(),
                                        parameterWithName("orderName").description("주문 이름 검색 키워드").optional(),
                                        parameterWithName("status").description("주문 상태 (PENDING, COMPLETED, FAILED, CANCELLED)").optional(),
                                        parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                                        parameterWithName("size").description("페이지당 항목 수").optional(),
                                        parameterWithName("sort").description("정렬 기준 (예: createdAt,desc)").optional()
                                ),
                                responseFields(
                                        fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("주문 목록"),
                                        fieldWithPath("content[].orderId").type(JsonFieldType.STRING).description("주문 ID"),
                                        fieldWithPath("content[].orderName").type(JsonFieldType.STRING).description("주문 이름"),
                                        fieldWithPath("content[].performanceName").type(JsonFieldType.STRING).description("공연 이름"),
                                        fieldWithPath("content[].performancePlace").type(JsonFieldType.STRING).description("공연 장소"),
                                        fieldWithPath("content[].performanceShowtime").type(JsonFieldType.STRING).description("공연 시간"),
                                        fieldWithPath("content[].amount").type(JsonFieldType.NUMBER).description("결제 금액"),
                                        fieldWithPath("content[].status").type(JsonFieldType.STRING).description("주문 상태"),
                                        fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("주문 생성 시간"),
                                        fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                        fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("비정렬 여부"),
                                        fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 없음 여부"),
                                        fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                        fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                                        fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("비페이징 여부"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 항목 수"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                        fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("비정렬 여부"),
                                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 없음 여부"),
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지 항목 수"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부")
                                )
                        )
                );
    }

    @Test
    @DisplayName("주문 상세 조회 API 호출 시")
    void getOrderDetail() throws Exception {
        // Given
        String orderId = "ORDER123";
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
                orderId,
                "테스트 공연 - A-12",
                new OrderDetailResponse.PerformanceInfo(
                        "테스트 공연",
                        "테스트 장소",
                        ZonedDateTime.now().plusDays(1)
                ),
                new OrderDetailResponse.SeatInfo(
                        "VIP",
                        "VIP",
                        "A-12"
                ),
                new OrderDetailResponse.CustomerInfo(
                        "test@example.com",
                        "테스트 사용자",
                        "010-1234-5678"
                ),
                new OrderDetailResponse.PaymentInfo(
                        "PAYMENT123",
                        "CARD",
                        new BigDecimal("100.00"),
                        "APPROVED",
                        ZonedDateTime.now()
                ),
                "COMPLETED",
                ZonedDateTime.now()
        );

        given(orderQueryService.getOrderDetail(any(Member.class), eq(orderId)))
                .willReturn(orderDetailResponse);

        // When
        ResultActions result = mockMvc.perform(
                get("/api/v1/orders/{orderId}", orderId)
                        .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                                ),
                                pathParameters(
                                        parameterWithName("orderId").description("주문 ID")
                                ),
                                responseFields(
                                        fieldWithPath("orderId").type(JsonFieldType.STRING).description("주문 ID"),
                                        fieldWithPath("orderName").type(JsonFieldType.STRING).description("주문 이름"),
                                        fieldWithPath("performance").type(JsonFieldType.OBJECT).description("공연 정보"),
                                        fieldWithPath("performance.name").type(JsonFieldType.STRING).description("공연 이름"),
                                        fieldWithPath("performance.place").type(JsonFieldType.STRING).description("공연 장소"),
                                        fieldWithPath("performance.showtime").type(JsonFieldType.STRING).description("공연 시간"),
                                        fieldWithPath("seat").type(JsonFieldType.OBJECT).description("좌석 정보"),
                                        fieldWithPath("seat.zone").type(JsonFieldType.STRING).description("좌석 구역"),
                                        fieldWithPath("seat.grade").type(JsonFieldType.STRING).description("좌석 등급"),
                                        fieldWithPath("seat.number").type(JsonFieldType.STRING).description("좌석 번호"),
                                        fieldWithPath("customer").type(JsonFieldType.OBJECT).description("고객 정보"),
                                        fieldWithPath("customer.email").type(JsonFieldType.STRING).description("고객 이메일"),
                                        fieldWithPath("customer.name").type(JsonFieldType.STRING).description("고객 이름"),
                                        fieldWithPath("customer.mobilePhone").type(JsonFieldType.STRING).description("고객 전화번호"),
                                        fieldWithPath("payment").type(JsonFieldType.OBJECT).description("결제 정보"),
                                        fieldWithPath("payment.paymentKey").type(JsonFieldType.STRING).description("결제 키"),
                                        fieldWithPath("payment.method").type(JsonFieldType.STRING).description("결제 수단"),
                                        fieldWithPath("payment.amount").type(JsonFieldType.NUMBER).description("결제 금액"),
                                        fieldWithPath("payment.status").type(JsonFieldType.STRING).description("결제 상태"),
                                        fieldWithPath("payment.approvedAt").type(JsonFieldType.STRING).description("결제 승인 시간"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("주문 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("주문 생성 시간")
                                )
                        )
                );
    }
}