package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shishmakov.persistence.entity.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, ObjectId> {

    @Query(value = "{}", fields = "{'_id': 1, 'title': 1, 'isbn': 1, 'comments': 1}")
    Flux<Book> findAllWithFetchComments();

    @Query(value = "{}", fields = "{'_id': 1, 'title': 1, 'isbn': 1, 'genres': 1, 'authors': 1}")
    Flux<Book> findAllWithFetchGenresAuthors();

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'authors': 1}")
    Mono<Book> findByIdWithFetchAuthors(ObjectId bookId);

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'genres': 1}")
    Mono<Book> findByIdWithFetchGenres(ObjectId bookId);

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'comments': 1}")
    Mono<Book> findByIdWithFetchComments(ObjectId bookId);

    @Query(value = "{'_id': ?0}", fields = "{'_id': -1, 'title': 1, 'isbn': 1, 'genres': 1, 'authors': 1}")
    Mono<Book> findByIdWithFetchGenresAuthors(ObjectId bookId);
}
