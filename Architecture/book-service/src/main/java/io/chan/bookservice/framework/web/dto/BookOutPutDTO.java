package io.chan.bookservice.framework.web.dto;

import io.chan.bookservice.domain.model.Book;

public record BookOutPutDTO (
    long bookNo,
    String bookTitle,
    String bookStatus
){
    public static BookOutPutDTO from(final Book loadBook) {
        return new BookOutPutDTO(loadBook.getNo(), loadBook.getTitle(), loadBook.getStatus().name());
    }
}
