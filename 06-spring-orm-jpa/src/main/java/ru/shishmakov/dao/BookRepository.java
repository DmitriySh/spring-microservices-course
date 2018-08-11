package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class BookRepository {
    private EntityManager em;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    public void save(Book book, List<Author> authors, List<Genre> genres) {
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
        }
    }

    public void delete(long bookId) {
        em.getTransaction().begin();
        try {
            Book book = em.getReference(Book.class, bookId); // proxy
            em.remove(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Optional<Book> getById(long bookId, Map<String, Object> context) {
        EntityGraph<Book> graph = em.createEntityGraph(Book.class);
        context.entrySet().stream()
                .filter(e -> Objects.equals(e.getKey(), "eager")) //eager
                .map(Entry::getValue)
                .map(List.class::cast)
                .flatMap(Collection::stream)
                .map(String::valueOf)
                .forEach(graph::addSubgraph);
        return ofNullable(em.find(Book.class, bookId, singletonMap("javax.persistence.fetchgraph", graph)));
    }

    public Collection<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class) // lazy loading
                .getResultList();
    }
}
