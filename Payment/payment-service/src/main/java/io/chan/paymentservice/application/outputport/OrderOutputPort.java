package io.chan.paymentservice.application.outputport;

import io.chan.paymentservice.domain.Order;

public interface OrderOutputPort {
    void saveOrder(Order order);
}
