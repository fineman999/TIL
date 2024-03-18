package io.chan.bookservice.application.outputport;

import io.chan.bookservice.domain.model.Book;

public interface BookOutPutPort {
    Book loadBook(long bookNo);
    Book save(Book book);
}
