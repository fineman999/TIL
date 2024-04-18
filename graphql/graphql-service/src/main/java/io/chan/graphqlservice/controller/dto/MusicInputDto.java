package io.chan.graphqlservice.controller.dto;

import io.chan.graphqlservice.domain.Music;

import java.time.ZonedDateTime;

public record MusicInputDto (
    String artist,
    String genre,
    ZonedDateTime releaseDate
){
    public Music toEntity() {
        return Music.builder()
            .artist(artist)
            .genre(genre)
            .releaseDate(releaseDate)
            .build();
    }
}
