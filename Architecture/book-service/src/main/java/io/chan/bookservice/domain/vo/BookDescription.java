package io.chan.bookservice.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookDescription {
    private String description;
    private String author;
    private String isbn;
    private LocalDateTime publishedAt;
    private BookSource source;

    public static BookDescription create(
        String description,
        String author,
        String isbn,
        LocalDateTime publishedAt,
        BookSource source
    ) {
        return new BookDescription(description, author, isbn, publishedAt, source);
    }
}
