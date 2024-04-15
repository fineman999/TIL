package io.chan.graphqlservice.repository;

import io.chan.graphqlservice.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicJpaRepository extends JpaRepository<Music, Long> {
}
