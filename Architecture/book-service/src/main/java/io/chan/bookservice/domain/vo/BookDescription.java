package io.chan.bookservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
