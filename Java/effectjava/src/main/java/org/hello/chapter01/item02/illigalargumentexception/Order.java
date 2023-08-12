package org.hello.chapter01.item02.illigalargumentexception;

import java.time.LocalDate;

public class Order {

    public void updateDeliveryDate(LocalDate localDate) throws IllegalArgumentException, NullPointerException {
        if (localDate == null) {
            throw new NullPointerException("배송일은 null이 될 수 없습니다.");
        }
        if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("배송일은 오늘 " + LocalDate.now() + "이후로 설정해주세요.");
        }
    }
}
