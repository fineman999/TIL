package io.chan.queuingsystemforjava.domain.payment.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.OrderStatus;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.payment.*;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.repository.*;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.seat.SeatStatus;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import io.chan.queuingsystemforjava.global.config.ObjectMapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({
  PaymentPersistenceService.class,
  OrderRepository.class,
  PerformanceRepository.class,
  SeatRepository.class,
  ZoneRepository.class,
  SeatGradeRepository.class,
  ObjectMapperConfig.class
})
public class PaymentPersistenceServiceTest {
  @Autowired private TestEntityManager testEntityManager;
  @Autowired private PaymentPersistenceService paymentPersistenceService;
  @Autowired private OrderRepository orderRepository;
  @Autowired private PerformanceRepository performanceRepository;
  @Autowired private SeatRepository seatRepository;
  @Autowired private ObjectMapper objectMapper;

  private Member member;
  private Performance performance;
  private Zone zone;
  private SeatGrade seatGrade;
  private Seat seat;
  private Order order;
  private PaymentResponse paymentResponse;

  @BeforeEach
  void setUpBase() {
    setUpMember();
    setUpPerformance();
    setUpZone();
    setUpSeatGrade();
    setUpSeat();
    setUpOrder();
    setUpPaymentResponse();
  }

  private void setUpMember() {
    member =
        Member.builder()
            .email("test@gmail.com")
            .password("testpassword")
            .memberRole(MemberRole.USER)
            .build();
    testEntityManager.persistAndFlush(member);
  }

  private void setUpPerformance() {
    performance =
        Performance.builder()
            .performanceName("공연")
            .performancePlace("장소")
            .performanceShowtime(
                ZonedDateTime.of(2025, 4, 12, 14, 30, 0, 0, ZoneId.of("Asia/Seoul")))
            .build();
    testEntityManager.persistAndFlush(performance);
  }

  private void setUpZone() {
    zone = Zone.builder().zoneName("VIP").performance(performance).build();
    testEntityManager.persistAndFlush(zone);
  }

  private void setUpSeatGrade() {
    seatGrade =
        SeatGrade.builder()
            .performance(performance)
            .gradeName("Grade1")
            .price(new BigDecimal("1000.00"))
            .build();
    testEntityManager.persistAndFlush(seatGrade);
  }

  private void setUpSeat() {
    seat =
        Seat.builder()
            .member(member)
            .seatCode("A01")
            .zone(zone)
            .seatStatus(SeatStatus.SELECTED)
            .seatGrade(seatGrade)
            .seatGradeId(seatGrade.getSeatGradeId())
            .build();
    testEntityManager.persistAndFlush(seat);
  }

  private void setUpOrder() {
    order =
        Order.create(
            "ORDER_12345",
            performance,
            seat,
            seatGrade.getPrice(),
            "customer@example.com",
            "김고객",
            "01012341234",
            "공연 티켓 - 좌석 A01",
            OrderStatus.PENDING);
    testEntityManager.persistAndFlush(order);
  }

  private void setUpPaymentResponse() {
    paymentResponse =
        new PaymentResponse(
            "2022-11-16",
            "5EnNZRJGvaBX7zk2yd8ydw26XvwXkLrx9POLqKQjmAw4b0e1",
            "NORMAL",
            "ORDER_12345",
            "공연 티켓 - 좌석 A01",
            "tosspayments",
            "KRW",
            "카드",
            new BigDecimal("1000.00"),
            new BigDecimal("1000.00"),
            "DONE",
            OffsetDateTime.parse("2024-02-13T12:17:57+09:00"),
            OffsetDateTime.parse("2024-02-13T12:18:14+09:00"),
            false,
            "9C62B18EEF0DE3EB7F4422EB6D14BC6E",
            new BigDecimal("909.00"),
            new BigDecimal("91.00"),
            false,
            new BigDecimal("0.00"),
            0,
            List.of(
                new PaymentResponse.Cancel(
                    new BigDecimal("500.00"),
                    "고객 요청",
                    new BigDecimal("0.00"),
                    0,
                    new BigDecimal("500.00"),
                    new BigDecimal("0.00"),
                    new BigDecimal("0.00"),
                    OffsetDateTime.parse("2024-02-13T12:20:00+09:00"),
                    "CANCEL_TX_123",
                    "RECEIPT_123",
                    "DONE",
                    "CANCEL_REQ_123",
                    true)),
            new PaymentResponse.Card(
                new BigDecimal("1000.00"),
                "71",
                "71",
                "12345678****000*",
                0,
                "00000000",
                false,
                "신용",
                "개인",
                "READY",
                false,
                null),
            new PaymentResponse.VirtualAccount(
                "NORMAL",
                "1234567890",
                "01",
                "김고객",
                "2024-02-14",
                "NONE",
                false,
                "PENDING",
                new PaymentResponse.VirtualAccount.RefundReceiveAccount("01", "987654321", "김고객"),
                "secret123"),
            new PaymentResponse.MobilePhone("01012341234", "PENDING", "https://receipt.url"),
            new PaymentResponse.GiftCertificate("12345678", "PENDING"),
            new PaymentResponse.Transfer("01", "PENDING"),
            new PaymentResponse.EasyPay("토스페이", new BigDecimal("0.00"), new BigDecimal("0.00")),
            new PaymentResponse.CashReceipt(
                "INCOME",
                "RECEIPT_123",
                "123456789",
                "https://receipt.url",
                new BigDecimal("1000.00"),
                new BigDecimal("0.00")),
            List.of(
                new PaymentResponse.CashReceipt(
                    "INCOME",
                    "RECEIPT_456",
                    "987654321",
                    "https://receipt.url/2",
                    new BigDecimal("500.00"),
                    new BigDecimal("0.00"))),
            new PaymentResponse.Discount(50),
            new PaymentResponse.Receipt("https://receipt.url"),
            new PaymentResponse.Checkout("https://checkout.url"));
  }

  @Nested
  @DisplayName("savePayment 메서드를 호출할 때")
  class SavePaymentTest {

    @Test
    @DisplayName("모든 중첩 객체를 포함한 결제 정보가 성공적으로 저장되고 주문 상태가 완료로 변경된다")
    void savePayment_successWithAllNestedObjects() {
      // When
      paymentPersistenceService.savePayment(paymentResponse);

      // Then
      Payment savedPayment =
          testEntityManager.find(Payment.class, findPaymentIdByKey(paymentResponse.paymentKey()));
      assertThat(savedPayment).isNotNull();

      PaymentCard savedCard =
          testEntityManager.find(PaymentCard.class, findCardIdByPayment(savedPayment));
      PaymentVirtualAccount savedVirtualAccount =
          testEntityManager.find(
              PaymentVirtualAccount.class, findVirtualAccountIdByPayment(savedPayment));
      PaymentMobilePhone savedMobilePhone =
          testEntityManager.find(
              PaymentMobilePhone.class, findMobilePhoneIdByPayment(savedPayment));
      PaymentGiftCertificate savedGiftCertificate =
          testEntityManager.find(
              PaymentGiftCertificate.class, findGiftCertificateIdByPayment(savedPayment));
      PaymentCashReceipt savedCashReceipt =
          testEntityManager.find(
              PaymentCashReceipt.class, findCashReceiptIdByPayment(savedPayment));
      PaymentDiscount savedDiscount =
          testEntityManager.find(PaymentDiscount.class, findDiscountIdByPayment(savedPayment));
      PaymentEasyPay savedEasyPay =
          testEntityManager.find(PaymentEasyPay.class, findEasyPayIdByPayment(savedPayment));
      PaymentTransfer savedTransfer =
          testEntityManager.find(PaymentTransfer.class, findTransferIdByPayment(savedPayment));

      assertAll(
          "결제 및 중첩 객체 검증",
          // Payment 검증
          () -> assertThat(savedPayment.getPaymentKey()).isEqualTo(paymentResponse.paymentKey()),
          () ->
              assertThat(savedPayment.getOrder().getOrderId()).isEqualTo(paymentResponse.orderId()),
          () -> assertThat(savedPayment.getStatus()).isEqualTo(paymentResponse.status()),
          () -> assertThat(savedPayment.getTotalAmount()).isEqualTo(paymentResponse.totalAmount()),
          () -> assertThat(savedPayment.getMethod()).isEqualTo(paymentResponse.method()),
          () ->
              assertThat(savedPayment.getRequestedAt().toInstant())
                  .isEqualTo(paymentResponse.requestedAt().toInstant()),
          () ->
              assertThat(savedPayment.getApprovedAt().toInstant())
                  .isEqualTo(paymentResponse.approvedAt().toInstant()),

          // PaymentCard 검증
          () -> assertThat(savedCard).isNotNull(),
          () -> assertThat(savedCard.getAmount()).isEqualTo(paymentResponse.card().amount()),
          () ->
              assertThat(savedCard.getIssuerCode()).isEqualTo(paymentResponse.card().issuerCode()),
          () -> assertThat(savedCard.getNumber()).isEqualTo(paymentResponse.card().number()),
          () -> assertThat(savedCard.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentVirtualAccount 검증
          () -> assertThat(savedVirtualAccount).isNotNull(),
          () ->
              assertThat(savedVirtualAccount.getAccountNumber())
                  .isEqualTo(paymentResponse.virtualAccount().accountNumber()),
          () ->
              assertThat(savedVirtualAccount.getBankCode())
                  .isEqualTo(paymentResponse.virtualAccount().bankCode()),
          () ->
              assertThat(savedVirtualAccount.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentMobilePhone 검증
          () -> assertThat(savedMobilePhone).isNotNull(),
          () ->
              assertThat(savedMobilePhone.getCustomerMobilePhone())
                  .isEqualTo(paymentResponse.mobilePhone().customerMobilePhone()),
          () ->
              assertThat(savedMobilePhone.getSettlementStatus())
                  .isEqualTo(paymentResponse.mobilePhone().settlementStatus()),
          () -> assertThat(savedMobilePhone.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentGiftCertificate 검증
          () -> assertThat(savedGiftCertificate).isNotNull(),
          () ->
              assertThat(savedGiftCertificate.getApproveNo())
                  .isEqualTo(paymentResponse.giftCertificate().approveNo()),
          () ->
              assertThat(savedGiftCertificate.getSettlementStatus())
                  .isEqualTo(paymentResponse.giftCertificate().settlementStatus()),
          () ->
              assertThat(savedGiftCertificate.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentCashReceipt 검증
          () -> assertThat(savedCashReceipt).isNotNull(),
          () ->
              assertThat(savedCashReceipt.getReceiptKey())
                  .isEqualTo(paymentResponse.cashReceipt().receiptKey()),
          () ->
              assertThat(savedCashReceipt.getAmount())
                  .isEqualTo(paymentResponse.cashReceipt().amount()),
          () -> assertThat(savedCashReceipt.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentDiscount 검증
          () -> assertThat(savedDiscount).isNotNull(),
          () ->
              assertThat(savedDiscount.getAmount()).isEqualTo(paymentResponse.discount().amount()),
          () -> assertThat(savedDiscount.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentEasyPay 검증
          () -> assertThat(savedEasyPay).isNotNull(),
          () ->
              assertThat(savedEasyPay.getProvider())
                  .isEqualTo(paymentResponse.easyPay().provider()),
          () -> assertThat(savedEasyPay.getAmount()).isEqualTo(paymentResponse.easyPay().amount()),
          () -> assertThat(savedEasyPay.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // PaymentTransfer 검증
          () -> assertThat(savedTransfer).isNotNull(),
          () ->
              assertThat(savedTransfer.getBankCode())
                  .isEqualTo(paymentResponse.transfer().bankCode()),
          () ->
              assertThat(savedTransfer.getSettlementStatus())
                  .isEqualTo(paymentResponse.transfer().settlementStatus()),
          () -> assertThat(savedTransfer.getPayment().getId()).isEqualTo(savedPayment.getId()));

      // Order 상태 검증
      Order updatedOrder = testEntityManager.find(Order.class, order.getId());
      assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("주문이 존재하지 않으면 예외가 발생한다")
    void savePayment_orderNotFound() {
      // Given
      PaymentResponse invalidResponse =
          new PaymentResponse(
              paymentResponse.version(),
              paymentResponse.paymentKey(),
              paymentResponse.type(),
              "INVALID_ORDER_ID",
              paymentResponse.orderName(),
              paymentResponse.mId(),
              paymentResponse.currency(),
              paymentResponse.method(),
              paymentResponse.totalAmount(),
              paymentResponse.balanceAmount(),
              paymentResponse.status(),
              paymentResponse.requestedAt(),
              paymentResponse.approvedAt(),
              paymentResponse.useEscrow(),
              paymentResponse.lastTransactionKey(),
              paymentResponse.suppliedAmount(),
              paymentResponse.vat(),
              paymentResponse.cultureExpense(),
              paymentResponse.taxFreeAmount(),
              paymentResponse.taxExemptionAmount(),
              paymentResponse.cancels(),
              paymentResponse.card(),
              paymentResponse.virtualAccount(),
              paymentResponse.mobilePhone(),
              paymentResponse.giftCertificate(),
              paymentResponse.transfer(),
              paymentResponse.easyPay(),
              paymentResponse.cashReceipt(),
              paymentResponse.cashReceipts(),
              paymentResponse.discount(),
              paymentResponse.receipt(),
              paymentResponse.checkout());

      // When & Then
      TicketingException exception =
          assertThrows(
              TicketingException.class,
              () -> {
                paymentPersistenceService.savePayment(invalidResponse);
              });
      assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }

    @Test
    @DisplayName("중첩 객체가 일부 null인 경우에도 결제 정보가 저장된다")
    void savePayment_withPartialNullNestedObjects() {
      // Given
      PaymentResponse minimalResponse =
          new PaymentResponse(
              "2022-11-16",
              "MINIMAL_PAYMENT_KEY",
              "NORMAL",
              order.getOrderId(),
              "공연 티켓 - 좌석 A01",
              "tosspayments",
              "KRW",
              "카드",
              new BigDecimal("1000.00"),
              new BigDecimal("1000.00"),
              "DONE",
              OffsetDateTime.parse("2024-02-13T12:17:57+09:00"),
              OffsetDateTime.parse("2024-02-13T12:18:14+09:00"),
              false,
              "9C62B18EEF0DE3EB7F4422EB6D14BC6E",
              new BigDecimal("909.00"),
              new BigDecimal("91.00"),
              false,
              new BigDecimal("0.00"),
              0,
              null,
              new PaymentResponse.Card(
                  new BigDecimal("1000.00"),
                  "71",
                  "71",
                  "12345678****000*",
                  0,
                  "00000000",
                  false,
                  "신용",
                  "개인",
                  "READY",
                  false,
                  null),
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null,
              null);

      // When
      paymentPersistenceService.savePayment(minimalResponse);

      // Then
      Payment savedPayment =
          testEntityManager.find(Payment.class, findPaymentIdByKey(minimalResponse.paymentKey()));
      assertThat(savedPayment).isNotNull();
      // ID를 먼저 조회
      Long cardId = findCardIdByPayment(savedPayment);
      Long virtualAccountId = findVirtualAccountIdByPayment(savedPayment);
      Long mobilePhoneId = findMobilePhoneIdByPayment(savedPayment);
      Long giftCertificateId = findGiftCertificateIdByPayment(savedPayment);
      Long cashReceiptId = findCashReceiptIdByPayment(savedPayment);
      Long discountId = findDiscountIdByPayment(savedPayment);
      Long easyPayId = findEasyPayIdByPayment(savedPayment);
      Long transferId = findTransferIdByPayment(savedPayment);

      // null이 아닌 경우에만 엔티티를 조회
      PaymentCard savedCard =
          cardId != null ? testEntityManager.find(PaymentCard.class, cardId) : null;
      PaymentVirtualAccount savedVirtualAccount =
          virtualAccountId != null
              ? testEntityManager.find(PaymentVirtualAccount.class, virtualAccountId)
              : null;
      PaymentMobilePhone savedMobilePhone =
          mobilePhoneId != null
              ? testEntityManager.find(PaymentMobilePhone.class, mobilePhoneId)
              : null;
      PaymentGiftCertificate savedGiftCertificate =
          giftCertificateId != null
              ? testEntityManager.find(PaymentGiftCertificate.class, giftCertificateId)
              : null;
      PaymentCashReceipt savedCashReceipt =
          cashReceiptId != null
              ? testEntityManager.find(PaymentCashReceipt.class, cashReceiptId)
              : null;
      PaymentDiscount savedDiscount =
          discountId != null ? testEntityManager.find(PaymentDiscount.class, discountId) : null;
      PaymentEasyPay savedEasyPay =
          easyPayId != null ? testEntityManager.find(PaymentEasyPay.class, easyPayId) : null;
      PaymentTransfer savedTransfer =
          transferId != null ? testEntityManager.find(PaymentTransfer.class, transferId) : null;

      assertAll(
          "결제 및 중첩 객체 검증",
          // Payment 검증
          () -> assertThat(savedPayment.getPaymentKey()).isEqualTo(minimalResponse.paymentKey()),
          () ->
              assertThat(savedPayment.getOrder().getOrderId()).isEqualTo(minimalResponse.orderId()),
          () -> assertThat(savedPayment.getStatus()).isEqualTo(minimalResponse.status()),

          // PaymentCard 검증
          () -> assertThat(savedCard).isNotNull(),
          () -> assertThat(savedCard.getAmount()).isEqualTo(minimalResponse.card().amount()),
          () -> assertThat(savedCard.getPayment().getId()).isEqualTo(savedPayment.getId()),

          // null 객체 검증
          () -> assertThat(savedVirtualAccount).isNull(),
          () -> assertThat(savedMobilePhone).isNull(),
          () -> assertThat(savedGiftCertificate).isNull(),
          () -> assertThat(savedCashReceipt).isNull(),
          () -> assertThat(savedDiscount).isNull(),
          () -> assertThat(savedEasyPay).isNull(),
          () -> assertThat(savedTransfer).isNull());

      // Order 상태 검증
      Order updatedOrder = testEntityManager.find(Order.class, order.getId());
      assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }
  }

  // Helper methods to find IDs
  private Long findPaymentIdByKey(String paymentKey) {
    return testEntityManager
        .getEntityManager()
        .createQuery("SELECT p.id FROM Payment p WHERE p.paymentKey = :paymentKey", Long.class)
        .setParameter("paymentKey", paymentKey)
        .getSingleResult();
  }

  private Long findCardIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery("SELECT c.id FROM PaymentCard c WHERE c.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findVirtualAccountIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery(
              "SELECT v.id FROM PaymentVirtualAccount v WHERE v.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findMobilePhoneIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery(
              "SELECT m.id FROM PaymentMobilePhone m WHERE m.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findGiftCertificateIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery(
              "SELECT g.id FROM PaymentGiftCertificate g WHERE g.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findCashReceiptIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery(
              "SELECT c.id FROM PaymentCashReceipt c WHERE c.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findDiscountIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery("SELECT d.id FROM PaymentDiscount d WHERE d.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findEasyPayIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery("SELECT e.id FROM PaymentEasyPay e WHERE e.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private Long findTransferIdByPayment(Payment payment) {
    try {
      return testEntityManager
          .getEntityManager()
          .createQuery("SELECT t.id FROM PaymentTransfer t WHERE t.payment = :payment", Long.class)
          .setParameter("payment", payment)
          .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }
}
