package io.chan.bookservice.domain.vo;

import java.time.LocalDateTime;

public class BookDescription {
    private String description;
    private String author;
    private String isbn;
    private LocalDateTime publishedAt;
    private BookSource source;
}
