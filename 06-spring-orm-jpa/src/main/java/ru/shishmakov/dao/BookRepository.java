package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;

@Repository
public class BookRepository {
    @PersistenceUnit
    private EntityManagerFactory emf;

    public long count() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select count(b.id) from Book b", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public long maxBookId() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select max(b.id) from Book b", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public void save(Book book, List<Author> authors, List<Genre> genres) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(book); // save entity
            book.getAuthors().addAll(authors);
            book.getGenres().addAll(genres);
            em.merge(book); // save references
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(long bookId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            getById(bookId, singletonMap("eager", singletonList("comments"))).ifPresent(b -> {
                em.merge(b);
                b.removeAllComment();
                em.remove(b);
            });
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Book> getById(long bookId, Map<String, Object> context) {
        EntityManager em = emf.createEntityManager();
        try {
            EntityGraph<Book> graph = em.createEntityGraph(Book.class);
            context.entrySet().stream()
                    .filter(e -> Objects.equals(e.getKey(), "eager")) //eager
                    .map(Entry::getValue)
                    .map(List.class::cast)
                    .flatMap(Collection::stream)
                    .map(String::valueOf)
                    .filter(Objects::nonNull)
                    .forEach(graph::addAttributeNodes);
            return ofNullable(em.find(Book.class, bookId, singletonMap("javax.persistence.loadgraph", graph)));
        } finally {
            em.close();
        }
    }

    public List<Book> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select b from Book b", Book.class) // lazy loading
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
