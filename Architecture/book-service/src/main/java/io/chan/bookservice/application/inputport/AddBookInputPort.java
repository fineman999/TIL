package io.chan.bookservice.application.inputport;

import io.chan.bookservice.application.outputport.BookOutPutPort;
import io.chan.bookservice.application.usecase.AddBookUseCase;
import io.chan.bookservice.domain.model.Book;
import io.chan.bookservice.domain.vo.BookLocation;
import io.chan.bookservice.domain.vo.BookSource;
import io.chan.bookservice.domain.vo.Classification;
import io.chan.bookservice.framework.web.dto.BookInfoDTO;
import io.chan.bookservice.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AddBookInputPort implements AddBookUseCase {
    private final BookOutPutPort bookOutPutPort;
    @Override
    public BookOutPutDTO addBook(final BookInfoDTO bookInfoDTO) {
        final Book book = Book.create(
                bookInfoDTO.title(),
                bookInfoDTO.author(),
                bookInfoDTO.isbn(),
                bookInfoDTO.description(),
                LocalDateTime.parse(bookInfoDTO.publicationDate()),
                BookSource.valueOf(bookInfoDTO.source()),
                Classification.valueOf(bookInfoDTO.classification()),
                BookLocation.valueOf(bookInfoDTO.location())
        );
        return BookOutPutDTO.from(bookOutPutPort.save(book));
    }
}
