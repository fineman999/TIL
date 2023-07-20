package org.hello.item01;

import org.hello.item01.enumeration.OrderStatus;

public class Order {

    private boolean prime;

    private boolean urgent;

    private Product product;

    public OrderStatus orderStatus;

    public static Order primeOrder(Product product) {
        Order order = new Order();
        order.prime = true;
        order.product = product;
        return order;
    }

    public static Order urgentOrder(Product product) {
        Order order = new Order();
        order.urgent = true;
        order.product = product;
        return order;
    }
}
