package io.chan.graphqlservice.repository;

import io.chan.graphqlservice.domain.Music;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class MusicRepository {
    private final MusicJpaRepository musicJpaRepository;

    public Music findById(Long id) {
        return musicJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No music found with id " + id));
    }

    public Music save(String artist, String genre, ZonedDateTime releaseDate) {
        Music music = Music.save(artist, genre, releaseDate);
        return musicJpaRepository.save(music);

    }
}
