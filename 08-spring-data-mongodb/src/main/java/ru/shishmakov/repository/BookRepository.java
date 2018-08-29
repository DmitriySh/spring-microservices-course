package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId> {

    //    @EntityGraph(type = LOAD, attributePaths = "authors")
//    @Query("SELECT b from Book b where b.id = :id")
//    Optional<Book> findByIdWithFetchAuthors(@Param("id") long bookId);

    //    @EntityGraph(type = LOAD, attributePaths = "genres")
//    @Query("SELECT b from Book b where b.id = :id")
//    Optional<Book> findByIdWithFetchGenres(@Param("id") long bookId);

    //    @EntityGraph(type = LOAD, attributePaths = "comments")
//    @Query("SELECT b from Book b where b.id = :id")
//    Optional<Book> findByIdWithFetchComments(@Param("id") long bookId);

    //    @EntityGraph(type = LOAD, attributePaths = {"comments", "genres", "authors"})
//    @Query("SELECT b from Book b where b.id = :id")
//    Optional<Book> findByIdWithFetchCommentsGenresAuthors(@Param("id") long bookId);
}
