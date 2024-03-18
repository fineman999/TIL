package io.chan.bookservice.application.usecase;

import io.chan.bookservice.framework.web.dto.BookOutPutDTO;

public interface InquiryUseCase {
    BookOutPutDTO getBookInfo(long bookNo);
}
