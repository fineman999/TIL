package io.chan.bestbookservice.persistence;

import io.chan.bestbookservice.domain.model.BestBook;
import io.chan.bestbookservice.domain.vo.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BestBookRepository extends MongoRepository<BestBook, String>{
    Optional<BestBook> findBestBookByItem(Item item);
}
