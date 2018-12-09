package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.shishmakov.persistence.entity.Author;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, ObjectId> {

    @Query(value = "{'_id': {$in : ?0}}", fields = "{'_id': 1, 'fullname': 1, 'books': 1}")
    Flux<Author> findAllByIdWithFetchBooks(Iterable<ObjectId> ids);
}
