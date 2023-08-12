package org.hello.item08.autocloseable;

import org.hello.chapter01.item08.autocloseable.AutoClosableIsGood;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AutoClosableIsGoodTest {

    @Test
    @DisplayName("AutoClosable 활용법")
    void name() {
        try (AutoClosableIsGood autoClosableIsGood = new AutoClosableIsGood()) {
            System.out.println("자원 반납 처리가 됨");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}