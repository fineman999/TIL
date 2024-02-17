package io.chan.cafekiosk.unit.order;

import io.chan.cafekiosk.unit.beverge.Beverage;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final LocalDateTime orderedAt;
    private final List<Beverage> beverages;

    public Order(LocalDateTime orderedAt, List<Beverage> beverages) {
        this.orderedAt = orderedAt;
        this.beverages = beverages;
    }
}
