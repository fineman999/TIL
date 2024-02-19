package io.chan.springrestdocspractice.books;

import java.time.LocalDateTime;

public record Book (
    Long id,
    String title,
    String author,
    LocalDateTime publishedAt
){
}
