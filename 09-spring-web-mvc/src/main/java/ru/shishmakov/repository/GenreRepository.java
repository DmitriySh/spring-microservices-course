package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @EntityGraph(type = LOAD, attributePaths = "books")
    @Query("SELECT g from Genre g where g.id = :id")
    Optional<Genre> findByIdWithFetchBooks(@Param("id") long genreId);

    @EntityGraph(type = LOAD, attributePaths = "books")
    @Query("SELECT g from Genre g where g.id in (:ids)")
    List<Genre> findAllByIdWithFetchBooks(@Param("ids") Iterable<Long> genreIds);
}
