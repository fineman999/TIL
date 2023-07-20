package org.hello.item01.enumeration;

public enum OrderStatus {
    PREPARING(0), SHIPPED(1), DELIVERED(2), CANCELED(3)
    ;

    private int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
