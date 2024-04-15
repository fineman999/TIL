package io.chan.graphqlservice.controller;

import io.chan.graphqlservice.controller.dto.MusicInputDto;
import io.chan.graphqlservice.controller.dto.MusicOutputDto;
import io.chan.graphqlservice.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MusicController {
    private final MusicService musicService;

    @QueryMapping
    public MusicOutputDto getMusicById(@Argument Long id) {
        return musicService.getMusicById(id);
    }

    @MutationMapping
    public MusicOutputDto createMusic(@Argument MusicInputDto musicInputDto) {
        return musicService.createMusic(musicInputDto);
    }
}
