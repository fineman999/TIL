package io.chan.bookservice.domain.model;

import io.chan.bookservice.domain.vo.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String title;

    @Embedded
    private BookDescription description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Classification classification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
