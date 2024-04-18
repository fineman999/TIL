package io.chan.graphqlservice.controller;

import io.chan.graphqlservice.controller.dto.AlbumOutputDto;
import io.chan.graphqlservice.controller.dto.MusicOutputDto;
import io.chan.graphqlservice.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AlbumController {
    private final MusicService musicService;

    @SchemaMapping(typeName = "Album", field = "musicList")
    public List<MusicOutputDto> getMusicList(AlbumOutputDto albumOutputDto, @Argument Long id) {
        return musicService.getMusicList(albumOutputDto, id);
    }
}
