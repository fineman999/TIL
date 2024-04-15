package io.chan.graphqlservice.controller.dto;

import java.time.ZonedDateTime;

public record MusicInputDto (
    String artist,
    String genre,
    ZonedDateTime releaseDate
){
}
