package io.chan.graphqlservice.controller.dto;

import io.chan.graphqlservice.domain.Album;
import io.chan.graphqlservice.domain.Music;

import java.time.ZonedDateTime;
import java.util.List;

public record AlbumInputDto(
        long id,
        String name,
        ZonedDateTime releaseDate,
        List<MusicInputDto> musics
) {

    public Album toEntity() {
        List<Music> newMusics = musics.stream().map(MusicInputDto::toEntity).toList();
        return Album.builder()
                .id(id)
                .name(name)
                .releaseDate(releaseDate)
                .musics(newMusics)
                .build();
    }
}
