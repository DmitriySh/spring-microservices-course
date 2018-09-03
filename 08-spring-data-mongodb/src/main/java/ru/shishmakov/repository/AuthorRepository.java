package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends MongoRepository<Author, ObjectId> {

    @Query(value = "{'_id': {$in: ?0}}", fields = "{'_id': -1}")
    List<Author> findAllByIdFetchId(Iterable<ObjectId> ids);
}
