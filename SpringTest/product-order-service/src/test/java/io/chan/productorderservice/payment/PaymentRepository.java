package io.chan.productorderservice.payment;

import java.util.HashMap;
import java.util.Map;

class PaymentRepository {

    private Map<Long, Payment> persistence = new HashMap<>();


    public void save(final Payment payment) {
        payment.assignId(1L);
        persistence.put(payment.getId(), payment);
    }
}
