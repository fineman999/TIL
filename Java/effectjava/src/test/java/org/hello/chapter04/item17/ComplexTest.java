package org.hello.chapter04.item17;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class ComplexTest {


    @Test
    @DisplayName("BigInt")
    void bigint() {
        BigInteger ten = BigInteger.TEN;
        BigInteger minusTen = ten.negate();
    }

    @Test
    @DisplayName("String")
    void string() {
        String name = "whiteship";
        StringBuilder stringBuilder = new StringBuilder(name); // 가변 동반 클래스
        stringBuilder.append(" is good");
        System.out.println(stringBuilder);
    }
}