package org.hello.item08.autocloseable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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