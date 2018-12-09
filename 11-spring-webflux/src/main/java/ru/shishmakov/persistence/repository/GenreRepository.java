package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.shishmakov.persistence.entity.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, ObjectId> {

}
