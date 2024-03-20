package io.chan.bestbookservice.web;

import io.chan.bestbookservice.domain.BestBookService;
import io.chan.bestbookservice.domain.model.BestBook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/best-books")
public class BestBookController {
    private final BestBookService bestBookService;

    @GetMapping
    public ResponseEntity<List<BestBook>> getAllBooks() {
        List<BestBook> books = bestBookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BestBook> getBookById(@PathVariable final String id) {
        BestBook book = bestBookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<BestBook> dealBestBook(@RequestBody final BestBook book) {
        BestBook bestBook = bestBookService.saveBook(book);
        return ResponseEntity.ok(bestBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BestBook> updateBook(@PathVariable final String id, @RequestBody final BestBook book) {
        BestBook bestBook = bestBookService.updateBook(id, book);
        return ResponseEntity.ok(bestBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable final String id) {
        bestBookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
