package io.chan.bookservice.application.inputport;

import io.chan.bookservice.application.outputport.BookOutPutPort;
import io.chan.bookservice.application.usecase.InquiryUseCase;
import io.chan.bookservice.domain.model.Book;
import io.chan.bookservice.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryInputPort implements InquiryUseCase {
    private final BookOutPutPort bookOutPort;
    @Override
    public BookOutPutDTO getBookInfo(final long bookNo) {
        Book loadBook = bookOutPort.loadBook(bookNo);
        return BookOutPutDTO.from(loadBook);
    }
}
