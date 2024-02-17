package io.chan.cafekiosk.unit;

import io.chan.cafekiosk.unit.beverge.Beverage;
import io.chan.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.List;

public class CafeKiosk {
    private final List<Beverage> beverages;

    public CafeKiosk(List<Beverage> beverages) {
        this.beverages = beverages;
    }

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int getTotalPrice() {
        return beverages.stream()
            .mapToInt(Beverage::getPrice)
            .sum();
    }

    public String getMenu() {
        return beverages.stream()
            .map(Beverage::getName)
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
    }

    public Order createOrder() {
        return new Order(LocalDateTime.now(), beverages);
    }

    public List<Beverage> getBeverages() {
        return beverages;
    }
}
