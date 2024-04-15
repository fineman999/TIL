package io.chan.graphqlservice.domain;

import lombok.Getter;

@Getter
public enum Genre {
    DANCE("댄스"),
    HIPHOP("힙합"),
    BALLAD("발라드"),
    ROCK("락"),
POP("팝");

    private final String genre;

    Genre(String genre) {
        this.genre = genre;
    }
}
