package io.chan.bookservice.application.usecase;

import io.chan.bookservice.framework.web.dto.BookInfoDTO;
import io.chan.bookservice.framework.web.dto.BookOutPutDTO;

public interface AddBookUseCase {
    BookOutPutDTO addBook(BookInfoDTO bookInfoDTO);
}
