package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId> {
    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'authors': 1}")
    Optional<Book> findByIdWithFetchAuthors(ObjectId bookId);

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'genres': 1}")
    Optional<Book> findByIdWithFetchGenres(ObjectId bookId);

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'comments': 1}")
    Optional<Book> findByIdWithFetchComments(ObjectId bookId);

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'genres': 1, 'authors': 1}")
    Optional<Book> findByIdWithFetchGenresAuthors(ObjectId bookId);
}
