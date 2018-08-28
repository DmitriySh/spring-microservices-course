package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

@Repository
public interface GenreRepository extends MongoRepository<Genre, ObjectId> {

}
