package io.chan.bookservice.application.inputport;

import io.chan.bookservice.application.outputport.BookOutPutPort;
import io.chan.bookservice.application.usecase.MakeAvailableUseCase;
import io.chan.bookservice.domain.model.Book;
import io.chan.bookservice.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MakeAvailableInputPort implements MakeAvailableUseCase {
    private final BookOutPutPort bookOutPutPort;
    @Override
    public BookOutPutDTO available(final Long bookNo) {
        Book loadBook = bookOutPutPort.loadBook(bookNo);
        loadBook.makeAvailable();
        return BookOutPutDTO.from(loadBook);
    }
}
