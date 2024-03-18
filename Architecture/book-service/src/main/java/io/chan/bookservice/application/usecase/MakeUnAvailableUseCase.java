package io.chan.bookservice.application.usecase;

import io.chan.bookservice.framework.web.dto.BookOutPutDTO;

public interface MakeUnAvailableUseCase {
    BookOutPutDTO unavailable(long bookNo);
}
