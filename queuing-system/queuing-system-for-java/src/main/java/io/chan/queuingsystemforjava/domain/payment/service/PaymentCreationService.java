package io.chan.queuingsystemforjava.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.payment.*;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentCreationService {
    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentCardJpaRepository paymentCardJpaRepository;
    private final PaymentEasyPayJpaRepository paymentEasyPayJpaRepository;
    private final PaymentCashReceiptJpaRepository paymentReceiptRepository;
    private final PaymentVirtualAccountJpaRepository paymentVirtualAccountJpaRepository;
    private final PaymentDiscountJpaRepository paymentDiscountJpaRepository;
    private final PaymentGiftCertificateJpaRepository paymentGiftCertificateJpaRepository;
    private final PaymentMobilePhoneJpaRepository paymentMobilePhoneJpaRepository;
    private final PaymentTransferJpaRepository paymentTransferJpaRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final PaymentCancelJpaRepository paymentCancelRepository;

    @Transactional
    public void savePayment(PaymentResponse response) {
        // Order 조회
        Order order = orderRepository.findByOrderId(response.orderId())
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_ORDER));
        order.markAsCompleted();
        // PaymentResponse -> Payment 변환
        Payment payment = objectMapper.convertValue(response, Payment.class);
        // 연관관계 수동 설정
        payment.setOrder(order);

        // Payment 저장
        paymentJpaRepository.save(payment);


    // Card 정보 저장
    if (response.card() != null) {
            PaymentCard card = objectMapper.convertValue(response.card(), PaymentCard.class);
            card.setPayment(payment); // 연관관계 설정
            paymentCardJpaRepository.save(card);
        }

        // EasyPay 정보 저장
        if (response.easyPay() != null) {
            PaymentEasyPay easyPay = objectMapper.convertValue(response.easyPay(), PaymentEasyPay.class);
            easyPay.setPayment(payment); // 연관관계 설정
            paymentEasyPayJpaRepository.save(easyPay);
        }

        // Receipt 정보 저장
        if (response.cashReceipt() != null) {
            PaymentCashReceipt receipt = objectMapper.convertValue(response.cashReceipt(), PaymentCashReceipt.class);
            receipt.setPayment(payment); // 연관관계 설정
            paymentReceiptRepository.save(receipt);
        }

        // VirtualAccount 정보 저장
         if (response.virtualAccount() != null) {
             PaymentVirtualAccount virtualAccount = objectMapper.convertValue(response.virtualAccount(), PaymentVirtualAccount.class);
                virtualAccount.setPayment(payment); // 연관관계 설정
                paymentVirtualAccountJpaRepository.save(virtualAccount);
         }

        // Discount 정보 저장
        if (response.discount() != null) {
            PaymentDiscount discount = objectMapper.convertValue(response.discount(), PaymentDiscount.class);
            discount.setPayment(payment); // 연관관계 설정
            paymentDiscountJpaRepository.save(discount);
        }

        // GiftCertificate 정보 저장
        if (response.giftCertificate() != null) {
            PaymentGiftCertificate giftCertificate = objectMapper.convertValue(response.giftCertificate(), PaymentGiftCertificate.class);
            giftCertificate.setPayment(payment); // 연관관계 설정
            paymentGiftCertificateJpaRepository.save(giftCertificate);
        }

        // MobilePhone 정보 저장
        if (response.mobilePhone() != null) {
            PaymentMobilePhone mobilePhone = objectMapper.convertValue(response.mobilePhone(), PaymentMobilePhone.class);
            mobilePhone.setPayment(payment); // 연관관계 설정
            paymentMobilePhoneJpaRepository.save(mobilePhone);
        }

        // Transfer 정보 저장
        if (response.transfer() != null) {
            PaymentTransfer transfer = objectMapper.convertValue(response.transfer(), PaymentTransfer.class);
            transfer.setPayment(payment); // 연관관계 설정
            paymentTransferJpaRepository.save(transfer);
        }
    }

    @Transactional
    public void savePaymentCancel(PaymentCancelResponse cancelResponse, Payment payment) {
        if (payment == null) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "Payment cannot be null");
        }
        List<PaymentResponse.Cancel> cancels = cancelResponse.cancels();
        if (cancels == null || cancels.isEmpty()) {
            throw new TicketingException(ErrorCode.INVALID_REQUEST, "No cancel records found in response");
        }

        payment.updateCancelPayment(cancelResponse);
        paymentJpaRepository.save(payment);

        List<PaymentCancel> newPaymentCancels = new ArrayList<>();
        for (PaymentResponse.Cancel cancel : cancels) {
            PaymentCancel paymentCancel = PaymentCancel.create(
                    payment,
                    cancel.cancelAmount(),
                    cancel.cancelReason(),
                    cancel.taxFreeAmount(),
                    cancel.taxExemptionAmount(),
                    cancel.refundableAmount(),
                    cancel.transferDiscountAmount(),
                    cancel.easyPayDiscountAmount(),
                    cancel.canceledAt(),
                    cancel.transactionKey(),
                    cancel.receiptKey(),
                    cancel.cancelStatus(),
                    cancel.cancelRequestId(),
                    cancel.isPartialCancelable()
            );
            newPaymentCancels.add(paymentCancel);
        }


        paymentCancelRepository.saveAll(newPaymentCancels);
    }
}