package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(type = LOAD, attributePaths = "books")
    @Query("SELECT a from Author a where a.id = :id")
    Optional<Author> findByIdWithFetchBooks(@Param("id") long authorId);

    @EntityGraph(type = LOAD, attributePaths = "books")
    @Query("SELECT a from Author a where a.id in (:ids)")
    List<Author> findAllByIdWithFetchBooks(@Param("ids") Iterable<Long> authorIds);
}
