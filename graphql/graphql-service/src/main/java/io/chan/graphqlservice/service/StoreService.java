package io.chan.graphqlservice.service;

import io.chan.graphqlservice.domain.Store;
import io.chan.graphqlservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public void decreaseV1(final Long id, final Long quantity) {
        Store store = storeRepository.getById(id);
        store.decrease(quantity);
    }
}
