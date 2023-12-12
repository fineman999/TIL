package io.chan.oauth2client.controller;

public record Photo(
        String photoId,
        String photoTitle,
        String photoDescription,
        String userId
) {
}
