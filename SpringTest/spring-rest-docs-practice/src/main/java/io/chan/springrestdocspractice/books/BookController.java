package io.chan.springrestdocspractice.books;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class BookController {

    @GetMapping("/books/{id}")
    public Book getBook(
            @PathVariable Long id
    ) {
        return new Book(1L, "Spring Boot in Action", "Craig Walls", LocalDateTime.now());
    }
}
