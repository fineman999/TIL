package io.chan.bookservice.framework.web;

import io.chan.bookservice.application.usecase.AddBookUseCase;
import io.chan.bookservice.application.usecase.InquiryUseCase;
import io.chan.bookservice.framework.web.dto.BookInfoDTO;
import io.chan.bookservice.framework.web.dto.BookOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final AddBookUseCase addBookUsecase;
    private final InquiryUseCase inquiryUsecase;

    @PostMapping
    public ResponseEntity<BookOutPutDTO> addBook(
            @RequestBody final BookInfoDTO bookInfoDTO
    ) {
        final BookOutPutDTO bookOutPutDTO = addBookUsecase.addBook(bookInfoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookOutPutDTO);
    }

    @GetMapping("/{bookNo}")
    public ResponseEntity<BookOutPutDTO> getBookInfo(
            @PathVariable final long bookNo
    ) {
        final BookOutPutDTO bookOutPutDTO = inquiryUsecase.getBookInfo(bookNo);
        return ResponseEntity.ok(bookOutPutDTO);
    }

}
