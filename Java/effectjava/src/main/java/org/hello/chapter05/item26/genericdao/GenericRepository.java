package org.hello.chapter05.item26.genericdao;

import java.util.Optional;
import java.util.Set;

public class GenericRepository<E extends Entity> {
    private final Set<E> entities;

    public GenericRepository(Set<E> entities) {
        this.entities = entities;
    }

    public Optional<E> findById(Long id) {
        return entities.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public void add(E entity) {
        entities.add(entity);
    }
}
