package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends MongoRepository<Genre, ObjectId> {

    @Query(value = "{'_id': {$in: ?0}}", fields = "{'_id': -1}")
    List<Genre> findAllByIdFetchId(Iterable<ObjectId> ids);
}
