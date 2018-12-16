package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.shishmakov.persistence.entity.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, ObjectId> {

    @Query(value = "{'_id': {$in : ?0}}", fields = "{'_id': 1, 'name': 1, 'books': 1}")
    Flux<Genre> findAllByIdWithFetchBooks(Iterable<ObjectId> ids);
}
