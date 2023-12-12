package io.chan.resourceserver.controller;

import lombok.Builder;

@Builder
public record Photo(
        String photoId,
        String photoTitle,
        String photoDescription,
        String userId
) {


    public static Photo of(String photoId, String photoTitle, String photoDescription, String userId) {
        return Photo.builder()
                .photoId(photoId)
                .photoTitle(photoTitle)
                .photoDescription(photoDescription)
                .userId(userId)
                .build();
    }
}
