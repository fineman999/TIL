package io.chan.graphqlservice.controller.dto;

import io.chan.graphqlservice.domain.Music;

import java.time.ZonedDateTime;

public record MusicOutputDto(
    Long id,
    String artist,
    String genre,
    String createdDate,
    ZonedDateTime releaseDate
){
    public static MusicOutputDto of(Music music) {
        return new MusicOutputDto(
            music.getId(),
            music.getArtist(),
            music.getGenre(),
            music.getCreatedAt().toString(),
            music.getReleaseDate()
        );
    }
}
