package io.chan.bookservice.domain.model;

import io.chan.bookservice.domain.vo.BookLocation;
import io.chan.bookservice.domain.vo.BookSource;
import io.chan.bookservice.domain.vo.BookStatus;
import io.chan.bookservice.domain.vo.Classification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    @DisplayName("도서 입고 생성")
    @Test
    void create() {
        // given
        final Book book = createBook();

        assertThat(book).isNotNull();
    }

    @DisplayName("도서 대여 가능 상태로 변경")
    @Test
    void makeAvailable() {
        final Book book = createBook();

        // when
        final Book availableBook = book.makeAvailable();

        // then
        assertThat(availableBook.getStatus()).isEqualTo(BookStatus.AVAILABLE);
    }

    @DisplayName("도서 대여 불가능 상태로 변경")
    @Test
    void makeUnavailable() {
        final Book book = createBook();

        // when
        final Book unavailableBook = book.makeUnavailable();

        // then
        assertThat(unavailableBook.getStatus()).isEqualTo(BookStatus.UNAVAILABLE);
    }

    private static Book createBook() {
        // given
        String title = "엔터프라이즈 아키텍처 패턴";
        String author = "마틴 파울러";
        String isbn = "9788966261211";
        String description = "엔터프라이즈 아키텍처 패턴을 이해하고 설계하는 방법을 설명한다.";
        Classification classification = Classification.COMPUTER;
        BookLocation gangnam = BookLocation.GANGNAM;
        BookSource bookSource = BookSource.SUPPLY;
        LocalDateTime publishedAt = LocalDateTime.parse("2013-12-01T00:00:00");

        return Book.create(
                title,
                author,
                isbn,
                description,
                publishedAt,
                bookSource,
                classification,
                gangnam
        );
    }
}