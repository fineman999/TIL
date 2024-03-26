package io.chan.bestbookservice.domain;

import io.chan.bestbookservice.domain.model.BestBook;
import io.chan.bestbookservice.domain.vo.Item;
import io.chan.bestbookservice.persistence.BestBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 서비스 구현
 * 유스케이스 구현 (베스트도서 처리)
 * 데이터 엑서스 계층 BestBookR1epository 직접 참조 dealBestBook
 * 품목으로 베스트 도서 검색하여 존재 시 베스트북 객체에 위임하여 대여횟수 증가 존재하지 않으면 베스트 도서 객체에 위임하여 베스트 도서로 최초등록
 * 비지니스 처리 끝나면 saveBook메소드로 저장
 */
@Service
@RequiredArgsConstructor
public class BestBookService {
    private final BestBookRepository bookRepository;

    public List<BestBook> getAllBooks() {
        return bookRepository.findAll();
    }
    public BestBook getBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    public void dealBestBook(Item item) {
        final BestBook bestBook = bookRepository.findBestBookByItem(item)
                .map(tempBook -> {
                    tempBook.increaseRentCount();
                    return tempBook;
                })
                .orElseGet(() -> {
                    BestBook newBook = BestBook.register(item, UUID.randomUUID());
                    return bookRepository.save(newBook);
                });
        bookRepository.save(bestBook);
    }

    public BestBook updateBook(String id, BestBook book) {
        final BestBook bestBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        bestBook.updateBook(book);
        return bookRepository.save(bestBook);
    }

    public void deleteBook(String id) {
        final BestBook bestBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        bookRepository.delete(bestBook);
    }

    public BestBook saveBook(BestBook book) {
        return bookRepository.save(book);
    }
}
