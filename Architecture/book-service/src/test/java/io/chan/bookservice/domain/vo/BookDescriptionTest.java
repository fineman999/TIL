package io.chan.bookservice.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookDescriptionTest {

    @DisplayName("책 설명을 생성한다.")
    @Test
    void create() {
        final BookDescription description = BookDescription.create(
                "엔터프라이즈 아키텍처패턴을 잘 설명해주는 도서",
                "마틴 파울러",
                "12312321",
                LocalDateTime.parse("2020-01-01T00:00:00"),
                BookSource.SUPPLY
        );

        assertThat(description).isNotNull();
    }
}