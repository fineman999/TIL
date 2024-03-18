package io.chan.bookservice.framework.jpaadapter;

import io.chan.bookservice.application.outputport.BookOutPutPort;
import io.chan.bookservice.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class BookAdapter implements BookOutPutPort {
    private final BookJpaRepository bookJpaRepository;
    @Override
    public Book loadBook(final long bookNo) {
        return bookJpaRepository.findById(bookNo)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    @Override
    public Book save(final Book book) {
        return bookJpaRepository.save(book);
    }
}
