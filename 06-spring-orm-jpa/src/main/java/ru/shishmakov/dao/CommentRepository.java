package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;

import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;

@Repository
public class CommentRepository {
    private EntityManager em;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    public List<Comment> getAll() {
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> getByIds(Set<Long> ids) {
        return em.createQuery("select c from Comment c where c.id in (:ids)", Comment.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public Optional<Comment> getById(long commentId, Map<String, Object> context) {
        EntityGraph<Comment> graph = em.createEntityGraph(Comment.class);
        context.entrySet().stream()
                .filter(e -> Objects.equals(e.getKey(), "eager")) //eager
                .map(Map.Entry::getValue)
                .map(List.class::cast)
                .flatMap(Collection::stream)
                .map(String::valueOf)
                .filter(Objects::nonNull)
                .forEach(graph::addAttributeNodes);
        return ofNullable(em.find(Comment.class, commentId, singletonMap("javax.persistence.loadgraph", graph)));
    }

    public void save(Comment comment, Book book) {
        em.getTransaction().begin();
        try {
            comment.setBook(book);
            em.persist(comment);
            book.addComment(comment); // performance: update context if 'comment' don't have cascade updates
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void delete(Comment comment) {
        em.getTransaction().begin();
        try {
            Book book = comment.getBook();
            em.remove(comment);
            book.removeComment(comment);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
