package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;

@Repository
public class CommentRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> getByIds(Set<Long> ids) {
        return em.createQuery("select c from Comment c where c.id in (:ids)", Comment.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public Optional<Comment> getById(long commentId, Map<String, List<String>> context) {
        EntityGraph<Comment> graph = em.createEntityGraph(Comment.class);
        context.entrySet().stream()
                .filter(e -> Objects.equals(e.getKey(), "eager")) //eager
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .forEach(graph::addAttributeNodes);
        return ofNullable(em.find(Comment.class, commentId, singletonMap("javax.persistence.loadgraph", graph)));
    }

    public void save(Comment comment, Book book) {
        comment.setBook(book);
        em.persist(comment);
        book.addComment(comment); // performance: update context if 'comment' don't have cascade updates
    }

    public void delete(Comment comment) {
        Book book = comment.getBook();
        em.remove(comment);
        book.removeComment(comment);
    }

    public long count() {
        return em.createQuery("select count(c.id) from Comment c", Long.class)
                .getSingleResult();
    }

    public long maxCommentId() {
        return em.createQuery("select max(c.id) from Comment c", Long.class)
                .getSingleResult();
    }
}
