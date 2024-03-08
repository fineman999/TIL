package io.chan.bookrentalservice.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IDNameTest {

    @DisplayName("IDName을 생성한다")
    @Test
    void createIDName() {
        // given
        final String id = "id";
        final String name = "name";

        // when
        final IDName idName = IDName.createIDName(id, name);

        // then
        assertThat(idName).isNotNull();
        assertThat(idName.getId()).isEqualTo(id);
        assertThat(idName.getName()).isEqualTo(name);
    }
}