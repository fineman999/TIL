package io.chan.cafekiosk.unit.beverge;

public class Latte implements Beverage {
    @Override
    public int getPrice() {
        return 4500;
    }

    @Override
    public String getName() {
        return "라떼";
    }
}
