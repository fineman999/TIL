package io.chan.graphqlservice.repository;

import io.chan.graphqlservice.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepository {
    private final StoreJpaRepository storeJpaRepository;


    public Store getById(Long id) {
        return storeJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No store found with id " + id));
    }

    public Store save(Store store) {
        return storeJpaRepository.save(store);
    }

    public void deleteAll() {
        storeJpaRepository.deleteAll();
    }
}
