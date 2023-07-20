package org.hello.item01.enumeration;

import org.hello.item01.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    @DisplayName("특정 enum 타입이 가질 수 있는 모든 값을 순회하며 출력")
    void values() {
        Arrays.stream(OrderStatus.values())
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("equals 체크 ==로 하자")
    void valueOf() {
        Order order = new Order();
        if (order.orderStatus == (OrderStatus.PREPARING)) {
                System.out.println("같다");
                return;
        }
        System.out.println("다르다");
    }
}