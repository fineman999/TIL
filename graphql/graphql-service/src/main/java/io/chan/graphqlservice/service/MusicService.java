package io.chan.graphqlservice.service;

import io.chan.graphqlservice.controller.dto.AlbumOutputDto;
import io.chan.graphqlservice.controller.dto.MusicInputDto;
import io.chan.graphqlservice.controller.dto.MusicOutputDto;
import io.chan.graphqlservice.domain.Music;
import io.chan.graphqlservice.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;
    public MusicOutputDto getMusicById(Long id) {
        Music music = musicRepository.findById(id);
        return MusicOutputDto.of(music);
    }

    public MusicOutputDto createMusic(String artist, String genre, ZonedDateTime releaseDate) {
        Music music = musicRepository.save(artist, genre, releaseDate);
        return MusicOutputDto.of(music);
    }

    public MusicOutputDto createMusic(MusicInputDto musicInputDto) {
        return createMusic(musicInputDto.artist(), musicInputDto.genre(), musicInputDto.releaseDate());
    }

    public List<MusicOutputDto> getMusicList(AlbumOutputDto albumOutputDto, Long id) {
throw new UnsupportedOperationException("Not implemented yet");
    }
}
