package ru.shishmakov.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(type = EntityGraphType.LOAD, attributePaths = "book")
    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    Optional<Comment> findByIdWithFetchBook(@Param("id") long commentId);

    @Query("SELECT coalesce(max(c.id), 0) FROM Comment c")
    long findMaxId();
}
