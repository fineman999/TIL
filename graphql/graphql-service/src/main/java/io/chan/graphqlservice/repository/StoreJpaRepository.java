package io.chan.graphqlservice.repository;

import io.chan.graphqlservice.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<Store, Long> {
}
