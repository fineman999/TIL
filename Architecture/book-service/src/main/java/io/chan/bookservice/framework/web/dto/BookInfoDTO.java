package io.chan.bookservice.framework.web.dto;

public record BookInfoDTO (
    String title,
    String description,
    String author,
    String isbn,
    String publicationDate,
    String source,
    String classification,
    String location
){

}
