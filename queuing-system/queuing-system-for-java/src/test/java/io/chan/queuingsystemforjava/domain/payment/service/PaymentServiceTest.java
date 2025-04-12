package io.chan.queuingsystemforjava.domain.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.exception.PaymentExceptionInterceptor;
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
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockRestServiceServer
@ActiveProfiles("test")
class PaymentServiceTest {

  @Autowired private PaymentService paymentService;

  @Autowired private MockRestServiceServer mockServer;

  private final String paymentKey = "5EnNZRJGvaBX7zk2yd8ydw26XvwXkLrx9POLqKQjmAw4b0e1";
  private final String orderId = "a4CWyWY5m89PNh7xJwhk1";
  private final long amount = 1000;

  @BeforeEach
  void setUp() {
    // Reset the server before each test
    mockServer.reset();
  }

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
  }

  @Nested
  @DisplayName("confirmPayment 메서드를 호출할 때")
  class ConfirmPayment {

    @Test
    @DisplayName("결제 확인이 성공적으로 완료된다")
    void confirmPayment_Success() {
      String successJson =
          """
                {
                    "mId": "tosspayments",
                    "paymentKey": "%s",
                    "orderId": "%s",
                    "orderName": "토스 티셔츠 외 2건",
                    "status": "DONE",
                    "requestedAt": "2024-02-13T12:17:57+09:00",
                    "approvedAt": "2024-02-13T12:18:14+09:00",
                    "useEscrow": false,
                    "currency": "KRW",
                    "totalAmount": %d,
                    "balanceAmount": %d,
                    "suppliedAmount": 909,
                    "vat": 91,
                    "taxFreeAmount": 0,
                    "method": "카드",
                    "card": {
                        "issuerCode": "71",
                        "acquirerCode": "71",
                        "number": "12345678****000*",
                        "installmentPlanMonths": 0,
                        "isInterestFree": false,
                        "approveNo": "00000000",
                        "cardType": "신용",
                        "ownerType": "개인",
                        "acquireStatus": "READY",
                        "amount": 1000
                    },
                    "easyPay": {
                        "provider": "토스페이",
                        "amount": 0,
                        "discountAmount": 0
                    },
                    "receipt": {
                        "url": "https://dashboard.tosspayments.com/receipt/redirection"
                    },
                    "checkout": {
                        "url": "https://api.tosspayments.com/v1/payments/%s/checkout"
                    }
                }
                """
              .formatted(paymentKey, orderId, amount, amount, paymentKey);

      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(withSuccess(successJson, MediaType.APPLICATION_JSON));

      PaymentResponse response = paymentService.confirmPayment(paymentKey, orderId, amount);

      assertThat(response).isNotNull();
      assertThat(response.status()).isEqualTo("DONE");
      assertThat(response.paymentKey()).isEqualTo(paymentKey);
      assertThat(response.orderId()).isEqualTo(orderId);
      assertThat(response.totalAmount()).isEqualTo(amount);
      mockServer.verify();
    }

    @Test
    @DisplayName("INVALID_REQUEST 에러로 인해 IllegalArgumentException이 발생한다")
    void confirmPayment_InvalidRequest_ThrowsIllegalArgumentException() {
      String errorJson =
          """
                {"code":"INVALID_REQUEST","message":"잘못된 요청입니다."}
                """;
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.BAD_REQUEST)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));

      assertThatThrownBy(() -> paymentService.confirmPayment(paymentKey, orderId, amount))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Invalid payment request");
      mockServer.verify();
    }

    @Test
    @DisplayName("ALREADY_PROCESSED_PAYMENT 에러로 인해 IllegalStateException이 발생한다")
    void confirmPayment_AlreadyProcessed_ThrowsIllegalStateException() {
      String errorJson =
          """
                {"code":"ALREADY_PROCESSED_PAYMENT","message":"이미 처리된 결제 입니다."}
                """;
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.BAD_REQUEST)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));

      assertThatThrownBy(() -> paymentService.confirmPayment(paymentKey, orderId, amount))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Payment already processed");
      mockServer.verify();
    }

    @Test
    @DisplayName("UNAUTHORIZED_KEY 에러로 인해 PaymentException이 발생한다")
    void confirmPayment_UnauthorizedKey_ThrowsPaymentException() {
      String errorJson =
          """
                {"code":"UNAUTHORIZED_KEY","message":"인증되지 않은 시크릿 키 혹은 클라이언트 키 입니다."}
                """;
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.UNAUTHORIZED)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));

      assertThatThrownBy(() -> paymentService.confirmPayment(paymentKey, orderId, amount))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Unauthorized payment key");
      mockServer.verify();
    }

    @Test
    @DisplayName("PROVIDER_ERROR 발생 후 재시도하여 성공한다")
    void confirmPayment_ProviderError_RetrySuccess() {
      String errorJson =
          """
                {"code":"PROVIDER_ERROR","message":"This is temporary error."}
                """;
      String successJson =
          """
                {
                    "mId": "tosspayments",
                    "paymentKey": "%s",
                    "orderId": "%s",
                    "orderName": "토스 티셔츠 외 2건",
                    "status": "DONE",
                    "requestedAt": "2024-02-13T12:17:57+09:00",
                    "approvedAt": "2024-02-13T12:18:14+09:00",
                    "useEscrow": false,
                    "currency": "KRW",
                    "totalAmount": %d,
                    "balanceAmount": %d,
                    "suppliedAmount": 909,
                    "vat": 91,
                    "taxFreeAmount": 0,
                    "method": "카드",
                    "card": {
                        "issuerCode": "71",
                        "acquirerCode": "71",
                        "number": "12345678****000*",
                        "installmentPlanMonths": 0,
                        "isInterestFree": false,
                        "approveNo": "00000000",
                        "cardType": "신용",
                        "ownerType": "개인",
                        "acquireStatus": "READY",
                        "amount": 1000
                    },
                    "easyPay": {
                        "provider": "토스페이",
                        "amount": 0,
                        "discountAmount": 0
                    },
                    "receipt": {
                        "url": "https://dashboard.tosspayments.com/receipt/redirection"
                    },
                    "checkout": {
                        "url": "https://api.tosspayments.com/v1/payments/%s/checkout"
                    }
                }
                """
              .formatted(paymentKey, orderId, amount, amount, paymentKey);

      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.BAD_REQUEST)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(withSuccess(successJson, MediaType.APPLICATION_JSON));

      PaymentResponse response = paymentService.confirmPayment(paymentKey, orderId, amount);

      assertThat(response).isNotNull();
      assertThat(response.status()).isEqualTo("DONE");
      assertThat(response.paymentKey()).isEqualTo(paymentKey);
      assertThat(response.orderId()).isEqualTo(orderId);
      assertThat(response.totalAmount()).isEqualTo(amount);
      mockServer.verify();
    }

    @Test
    @DisplayName("PROVIDER_ERROR가 반복되어 재시도 실패 후 fallback이 호출된다")
    void confirmPayment_ProviderError_RetryFailure_Fallback() {
      String errorJson =
          """
                {"code":"PROVIDER_ERROR","message":"This is temporary error."}
                """;
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.BAD_REQUEST)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.BAD_REQUEST)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));
      mockServer
          .expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
          .andExpect(method(HttpMethod.POST))
          .andRespond(
              withStatus(HttpStatus.BAD_REQUEST)
                  .body(errorJson)
                  .contentType(MediaType.APPLICATION_JSON));

      assertThatThrownBy(() -> paymentService.confirmPayment(paymentKey, orderId, amount))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("This is temporary error.");
      mockServer.verify();
    }
  }
}
