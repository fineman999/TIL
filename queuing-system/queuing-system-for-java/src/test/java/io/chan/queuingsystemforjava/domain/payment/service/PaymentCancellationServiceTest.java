package io.chan.queuingsystemforjava.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.exception.PaymentExceptionInterceptor;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.processor.SimulatedPaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.repository.IdempotencyRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockRestServiceServer
@ActiveProfiles("test")
class PaymentCancellationServiceTest {

    @Autowired
    private PaymentCancellationService paymentCancellationService;

    @Autowired
    private MockRestServiceServer mockServer;

    @MockitoBean
    private IdempotencyRedisRepository idempotencyRedisRepository;

    private final String paymentKey = "5EnNZRJGvaBX7zk2yd8ydw26XvwXkLrx9POLqKQjmAw4b0e1";
    private final String orderId = "a4CWyWY5m89PNh7xJwhk1";
    private final String idempotencyKey = "idempotency-key-123";
    private final PaymentCancelRequest request = new PaymentCancelRequest(
            "테스트 결제 취소",
            BigDecimal.valueOf(1000),
            null,
            BigDecimal.ZERO,
            "KRW"
    );

    @TestConfiguration
    static class TestConfig {

        @Bean
        public PaymentApiClient paymentApiClient(RestClient restClient) {
            HttpServiceProxyFactory factory =
                    HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
            return factory.createClient(PaymentApiClient.class);
        }

        @Bean
        public RestClient restClient(RestClient.Builder builder, ObjectMapper objectMapper) {
            // Use the builder injected by Spring Boot Test
            return builder
                    .baseUrl("https://api.tosspayments.com")
                    .requestInterceptor(new PaymentExceptionInterceptor(objectMapper))
                    .build();
        }

        @Bean
        public PaymentProcessor paymentProcessor() {
            return new SimulatedPaymentProcessor();
        }
    }

    @BeforeEach
    void setUp() {
        mockServer.reset();
        when(idempotencyRedisRepository.getCachedResponse(anyString())).thenReturn(null);
    }

    @Nested
    @DisplayName("cancelPayment 메서드를 호출할 때")
    class CancelPayment {

        @Test
        @DisplayName("결제 취소가 성공적으로 완료된다")
        void cancelPayment_Success() {
            String successJson =
                    """
                            {
                                "mId": "tosspayments",
                                "lastTransactionKey": "090A796806E726BBB929F4A2CA7DB9A7",
                                "paymentKey": "%s",
                                "orderId": "%s",
                                "orderName": "토스 티셔츠 외 2건",
                                "taxExemptionAmount": 0,
                                "status": "CANCELED",
                                "requestedAt": "2024-02-13T12:17:57+09:00",
                                "approvedAt": "2024-02-13T12:18:14+09:00",
                                "useEscrow": false,
                                "cultureExpense": false,
                                "card": {
                                    "issuerCode": "71",
                                    "acquirerCode": "71",
                                    "number": "12345678****000*",
                                    "installmentPlanMonths": 0,
                                    "isInterestFree": false,
                                    "approveNo": "00000000",
                                    "useCardPoint": false,
                                    "cardType": "신용",
                                    "ownerType": "개인",
                                    "acquireStatus": "READY",
                                    "amount": 1000
                                },
                                "cancels": [
                                    {
                                        "transactionKey": "090A796806E726BBB929F4A2CA7DB9A7",
                                        "cancelReason": "테스트 결제 취소",
                                        "taxExemptionAmount": 0,
                                        "canceledAt": "2024-02-13T12:20:23+09:00",
                                        "transferDiscountAmount": 0,
                                        "easyPayDiscountAmount": 0,
                                        "cancelAmount": 1000,
                                        "taxFreeAmount": 0,
                                        "refundableAmount": 0,
                                        "cancelStatus": "DONE"
                                    }
                                ],
                                "type": "NORMAL",
                                "easyPay": {
                                    "provider": "토스페이",
                                    "amount": 0,
                                    "discountAmount": 0
                                },
                                "currency": "KRW",
                                "totalAmount": 1000,
                                "balanceAmount": 0,
                                "suppliedAmount": 0,
                                "vat": 0,
                                "taxFreeAmount": 0,
                                "method": "카드"
                            }
                            """.formatted(paymentKey, orderId);

            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andExpect(header("Idempotency-Key", idempotencyKey))
                    .andRespond(withSuccess(successJson, MediaType.APPLICATION_JSON));

            PaymentCancelResponse response = paymentCancellationService.cancelPayment(paymentKey, request, idempotencyKey);

            assertThat(response).isNotNull();
            assertThat(response.status()).isEqualTo("CANCELED");
            assertThat(response.paymentKey()).isEqualTo(paymentKey);
            assertThat(response.orderId()).isEqualTo(orderId);
            assertThat(response.cancels()).hasSize(1);
            assertThat(response.cancels().getFirst().cancelAmount()).isEqualTo(BigDecimal.valueOf(1000));
            mockServer.verify();
        }

        @Test
        @DisplayName("idempotency 키로 캐시된 응답이 있으면 캐시된 응답을 반환한다")
        void cancelPayment_CachedResponse_ReturnsCached() {
            PaymentCancelResponse cachedResponse = new PaymentCancelResponse(
                    "tosspayments",                     // mId
                    "090A796806E726BBB929F4A2CA7DB9A7", // lastTransactionKey
                    paymentKey,                         // paymentKey
                    orderId,                            // orderId
                    "토스 티셔츠 외 2건",                // orderName
                    0,                                  // taxExemptionAmount
                    "CANCELED",                         // status
                    OffsetDateTime.parse("2024-02-13T12:17:57+09:00"), // requestedAt
                    OffsetDateTime.parse("2024-02-13T12:18:14+09:00"), // approvedAt
                    false,                              // useEscrow
                    false,                              // cultureExpense
                    new PaymentResponse.Card(           // card
                            BigDecimal.valueOf(1000),       // amount
                            "71",                           // issuerCode
                            "71",                           // acquirerCode
                            "12345678****000*",             // number
                            0,                              // installmentPlanMonths
                            "00000000",                     // approveNo
                            false,                          // useCardPoint
                            "신용",                         // cardType
                            "개인",                         // ownerType
                            "READY",                        // acquireStatus
                            false,                          // isInterestFree
                            null                            // interestPayer
                    ),
                    null,                               // virtualAccount
                    null,                               // transfer
                    null,                               // mobilePhone
                    null,                               // giftCertificate
                    null,                               // cashReceipt
                    null,                               // cashReceipts
                    null,                               // discount
                    List.of(new PaymentResponse.Cancel( // cancels
                            BigDecimal.valueOf(1000),       // cancelAmount
                            "테스트 결제 취소",              // cancelReason
                            BigDecimal.ZERO,                // taxFreeAmount
                            0,                              // taxExemptionAmount
                            BigDecimal.ZERO,                // refundableAmount
                            BigDecimal.ZERO,                // transferDiscountAmount
                            BigDecimal.ZERO,                // easyPayDiscountAmount
                            OffsetDateTime.parse("2024-02-13T12:20:23+09:00"), // canceledAt
                            "090A796806E726BBB929F4A2CA7DB9A7", // transactionKey
                            null,                           // receiptKey
                            "DONE",                         // cancelStatus
                            null,                           // cancelRequestId
                            true                            // isPartialCancelable
                    )),
                    "NORMAL",                           // type
                    new PaymentResponse.EasyPay(        // easyPay
                            "토스페이",                      // provider
                            BigDecimal.ZERO,                // amount
                            BigDecimal.ZERO                 // discountAmount
                    ),
                    "KR",                               // country
                    true,                               // isPartialCancelable
                    new PaymentResponse.Receipt(        // receipt
                            "https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20240213121757MvuS8&ref=PX"
                    ),
                    new PaymentResponse.Checkout(       // checkout
                            "https://api.tosspayments.com/v1/payments/" + paymentKey + "/checkout"
                    ),
                    "KRW",                              // currency
                    BigDecimal.valueOf(1000),           // totalAmount
                    BigDecimal.ZERO,                    // balanceAmount
                    BigDecimal.ZERO,                    // suppliedAmount
                    BigDecimal.ZERO,                    // vat
                    BigDecimal.ZERO,                    // taxFreeAmount
                    "카드",                              // method
                    "2022-11-16"                        // version
            );

            when(idempotencyRedisRepository.getCachedResponse(idempotencyKey)).thenReturn(cachedResponse);

            PaymentCancelResponse response = paymentCancellationService.cancelPayment(paymentKey, request, idempotencyKey);

            assertThat(response).isEqualTo(cachedResponse);
            mockServer.verify();
        }

        @Test
        @DisplayName("INVALID_REQUEST 에러로 인해 TicketingException이 발생한다")
        void cancelPayment_InvalidRequest_ThrowsTicketingException() {
            String errorJson =
                    """
                            {"code":"INVALID_REQUEST","message":"잘못된 요청입니다."}
                            """;
            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(
                            withStatus(HttpStatus.BAD_REQUEST)
                                    .body(errorJson)
                                    .contentType(MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> paymentCancellationService.cancelPayment(paymentKey, request, idempotencyKey))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid payment request");
            mockServer.verify();
        }

        @Test
        @DisplayName("취소 사유가 없으면 TicketingException이 발생한다")
        void cancelPayment_NoCancelReason_ThrowsTicketingException() {
            PaymentCancelRequest invalidRequest = new PaymentCancelRequest(
                    null, BigDecimal.valueOf(1000), null, BigDecimal.ZERO, "KRW"
            );

            assertThatThrownBy(() -> paymentCancellationService.cancelPayment(paymentKey, invalidRequest, idempotencyKey))
                    .isInstanceOf(TicketingException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REQUEST)
                    .hasMessageContaining("Cancel reason is required");
        }

        @Test
        @DisplayName("환불 계좌 정보가 유효하지 않으면 TicketingException이 발생한다")
        void cancelPayment_InvalidRefundAccount_ThrowsTicketingException() {
            PaymentCancelRequest invalidRequest = new PaymentCancelRequest(
                    "테스트 결제 취소",
                    BigDecimal.valueOf(1000),
                    new PaymentCancelRequest.RefundReceiveAccount(null, "1234567890", "홍길동"),
                    BigDecimal.ZERO,
                    "KRW"
            );

            assertThatThrownBy(() -> paymentCancellationService.cancelPayment(paymentKey, invalidRequest, idempotencyKey))
                    .isInstanceOf(TicketingException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REQUEST)
                    .hasMessageContaining("Bank code is required for refund account");
        }

        @Test
        @DisplayName("PROVIDER_ERROR 발생 후 재시도하여 성공한다")
        void cancelPayment_ProviderError_RetrySuccess() {
            String errorJson =
                    """
                            {"code":"PROVIDER_ERROR","message":"This is temporary error."}
                            """;
            String successJson =
                    """
                            {
                                "mId": "tosspayments",
                                "lastTransactionKey": "090A796806E726BBB929F4A2CA7DB9A7",
                                "paymentKey": "%s",
                                "orderId": "%s",
                                "orderName": "토스 티셔츠 외 2건",
                                "taxExemptionAmount": 0,
                                "status": "CANCELED",
                                "requestedAt": "2024-02-13T12:17:57+09:00",
                                "approvedAt": "2024-02-13T12:18:14+09:00",
                                "useEscrow": false,
                                "cultureExpense": false,
                                "card": {
                                    "issuerCode": "71",
                                    "acquirerCode": "71",
                                    "number": "12345678****000*",
                                    "installmentPlanMonths": 0,
                                    "isInterestFree": false,
                                    "approveNo": "00000000",
                                    "useCardPoint": false,
                                    "cardType": "신용",
                                    "ownerType": "개인",
                                    "acquireStatus": "READY",
                                    "amount": 1000
                                },
                                "cancels": [
                                    {
                                        "transactionKey": "090A796806E726BBB929F4A2CA7DB9A7",
                                        "cancelReason": "테스트 결제 취소",
                                        "taxExemptionAmount": 0,
                                        "canceledAt": "2024-02-13T12:20:23+09:00",
                                        "transferDiscountAmount": 0,
                                        "easyPayDiscountAmount": 0,
                                        "cancelAmount": 1000,
                                        "taxFreeAmount": 0,
                                        "refundableAmount": 0,
                                        "cancelStatus": "DONE"
                                    }
                                ],
                                "type": "NORMAL",
                                "easyPay": {
                                    "provider": "토스페이",
                                    "amount": 0,
                                    "discountAmount": 0
                                },
                                "currency": "KRW",
                                "totalAmount": 1000,
                                "balanceAmount": 0,
                                "suppliedAmount": 0,
                                "vat": 0,
                                "taxFreeAmount": 0,
                                "method": "카드"
                            }
                            """.formatted(paymentKey, orderId);

            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(
                            withStatus(HttpStatus.BAD_REQUEST)
                                    .body(errorJson)
                                    .contentType(MediaType.APPLICATION_JSON));
            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withSuccess(successJson, MediaType.APPLICATION_JSON));

            PaymentCancelResponse response = paymentCancellationService.cancelPayment(paymentKey, request, idempotencyKey);

            assertThat(response).isNotNull();
            assertThat(response.status()).isEqualTo("CANCELED");
            assertThat(response.paymentKey()).isEqualTo(paymentKey);
            assertThat(response.orderId()).isEqualTo(orderId);
            assertThat(response.cancels()).hasSize(1);
            assertThat(response.cancels().getFirst().cancelAmount()).isEqualTo(BigDecimal.valueOf(1000));
            mockServer.verify();
        }

        @Test
        @DisplayName("PROVIDER_ERROR가 반복되어 재시도 실패 후 fallback이 호출된다")
        void cancelPayment_ProviderError_RetryFailure_Fallback() {
            String errorJson =
                    """
                            {"code":"PROVIDER_ERROR","message":"This is temporary error."}
                            """;
            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(
                            withStatus(HttpStatus.BAD_REQUEST)
                                    .body(errorJson)
                                    .contentType(MediaType.APPLICATION_JSON));
            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(
                            withStatus(HttpStatus.BAD_REQUEST)
                                    .body(errorJson)
                                    .contentType(MediaType.APPLICATION_JSON));
            mockServer
                    .expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(
                            withStatus(HttpStatus.BAD_REQUEST)
                                    .body(errorJson)
                                    .contentType(MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> paymentCancellationService.cancelPayment(paymentKey, request, idempotencyKey))
                    .isInstanceOf(TicketingException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PAYMENT_ERROR)
                    .hasMessageContaining("Payment cancellation failed after retries");
            mockServer.verify();
        }
    }
}