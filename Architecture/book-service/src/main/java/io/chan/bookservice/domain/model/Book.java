package io.chan.bookservice.domain.model;

import io.chan.bookservice.domain.vo.BookDescription;
import io.chan.bookservice.domain.vo.BookLocation;
import io.chan.bookservice.domain.vo.BookStatus;
import io.chan.bookservice.domain.vo.Classification;

public class Book {
    private Long no;
    private String title;
    private BookDescription description;
    private Classification classification;
    private BookStatus status;
    private BookLocation location;

}
