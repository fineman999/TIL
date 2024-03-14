package io.chan.bookservice.domain.model;

import io.chan.bookservice.domain.vo.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Book {
    private Long no;
    private String title;
    private BookDescription description;
    private Classification classification;
    private BookStatus status;
    private BookLocation location;


    public static Book create(
        String title,
        String author,
        String isbn,
        String description,
        LocalDateTime publishedAt,
        BookSource source,
        Classification classification,
        BookLocation location
    ) {
        final BookDescription newDesc = BookDescription.create(
                description,
                author,
                isbn,
                publishedAt,
                source
        );
        return Book.builder()
                .title(title)
                .description(newDesc)
                .classification(classification)
                .status(BookStatus.ENTERED)
                .location(location)
                .build();
    }

    public Book makeAvailable() {
        return Book.builder()
                .no(this.no)
                .title(this.title)
                .description(this.description)
                .classification(this.classification)
                .status(BookStatus.AVAILABLE)
                .location(this.location)
                .build();
    }

    public Book makeUnavailable() {
        return Book.builder()
                .no(this.no)
                .title(this.title)
                .description(this.description)
                .classification(this.classification)
                .status(BookStatus.UNAVAILABLE)
                .location(this.location)
                .build();
    }
}
