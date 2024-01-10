package io.chan.productorderservice.payment.adapter;

import io.chan.productorderservice.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PaymentRepository extends JpaRepository<Payment, Long> {

}
