package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, ObjectId> {

}
