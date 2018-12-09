package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, ObjectId> {

}
