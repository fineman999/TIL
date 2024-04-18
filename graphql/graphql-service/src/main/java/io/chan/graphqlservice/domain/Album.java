package io.chan.graphqlservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private ZonedDateTime releaseDate;

    @OneToMany(mappedBy = "album", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Music> musics;

}
