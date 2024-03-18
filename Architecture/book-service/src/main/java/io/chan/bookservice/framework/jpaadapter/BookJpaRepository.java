package io.chan.bookservice.framework.jpaadapter;

import io.chan.bookservice.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
}
