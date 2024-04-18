package io.chan.graphqlservice.controller.dto;

import io.chan.graphqlservice.domain.Album;

public record AlbumOutputDto(
        long id,
        String name,
        String releaseDate
) {
    public static AlbumOutputDto of(Album album) {
        return new AlbumOutputDto(
                album.getId(),
                album.getName(),
                album.getReleaseDate().toString()
        );
    }
}
