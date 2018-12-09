package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.persistence.entity.Genre;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, ObjectId> {

}
