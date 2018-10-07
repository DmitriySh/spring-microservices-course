package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext
    private EntityManager em;

    public long count() {
        return em.createQuery("select count(b.id) from Book b", Long.class).getSingleResult();
    }

    public long maxBookId() {
        return em.createQuery("select max(b.id) from Book b", Long.class).getSingleResult();
    }

    public void save(Book book, Collection<Author> authors, Collection<Genre> genres) {
        em.persist(book); // save entity
        book.getAuthors().addAll(authors);
        book.getGenres().addAll(genres);
        em.merge(book); // save references
    }

    public void delete(long bookId) {
        getById(bookId, singletonMap("eager", singletonList("comments"))).ifPresent(b -> {
            em.merge(b);
            b.removeAllComment();
            em.remove(b);
        });
    }

    public Optional<Book> getById(long bookId, Map<String, List<String>> context) {
        EntityGraph<Book> graph = em.createEntityGraph(Book.class);
        context.entrySet().stream()
                .filter(e -> Objects.equals(e.getKey(), "eager")) //eager
                .map(Entry::getValue)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .forEach(graph::addAttributeNodes);
        return ofNullable(em.find(Book.class, bookId, singletonMap("javax.persistence.loadgraph", graph)));
    }

    public List<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class) // lazy loading
                .getResultList();
    }
}
