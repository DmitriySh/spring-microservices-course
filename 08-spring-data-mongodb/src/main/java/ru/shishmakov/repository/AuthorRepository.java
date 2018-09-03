package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends MongoRepository<Author, ObjectId> {

    @Override
    List<Author> findAllById(Iterable<ObjectId> ids);
}
