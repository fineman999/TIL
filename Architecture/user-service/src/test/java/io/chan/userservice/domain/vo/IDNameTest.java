package io.chan.userservice.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDNameTest {

    @DisplayName("IDName 객체 생성 테스트")
    @Test
    void create() {
        IDName idName = IDName.create("1", "name");
        assertEquals("1", idName.getId());
        assertEquals("name", idName.getName());
    }
}