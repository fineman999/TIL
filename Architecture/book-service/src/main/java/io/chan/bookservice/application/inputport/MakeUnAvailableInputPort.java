package io.chan.bookservice.application.inputport;

import io.chan.bookservice.application.outputport.BookOutPutPort;
import io.chan.bookservice.application.usecase.MakeUnAvailableUseCase;
import io.chan.bookservice.domain.model.Book;
import io.chan.bookservice.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MakeUnAvailableInputPort implements MakeUnAvailableUseCase {
    private final BookOutPutPort bookOutPutPort;
    @Override
    public BookOutPutDTO unavailable(final long bookNo) {
        Book loadBook = bookOutPutPort.loadBook(bookNo);
        loadBook.makeUnavailable();
        return BookOutPutDTO.from(loadBook);
    }
}
