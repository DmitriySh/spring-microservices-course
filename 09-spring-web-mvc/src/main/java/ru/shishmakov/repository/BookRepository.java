package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @EntityGraph(type = LOAD, attributePaths = "authors")
    @Query("SELECT b from Book b where b.id = :id")
    Optional<Book> findByIdWithFetchAuthors(@Param("id") long bookId);

    @EntityGraph(type = LOAD, attributePaths = "genres")
    @Query("SELECT b from Book b where b.id = :id")
    Optional<Book> findByIdWithFetchGenres(@Param("id") long bookId);

    @EntityGraph(type = LOAD, attributePaths = {"genres", "authors"})
    @Query("SELECT b from Book b where b.id = :id")
    Optional<Book> findByIdWithFetchGenresAuthors(@Param("id") long bookId);
}
