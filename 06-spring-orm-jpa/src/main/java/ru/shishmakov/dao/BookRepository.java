package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

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
    private final GenreRepository genreDao;
    private final AuthorRepository authorDao;

    private EntityManager em;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    public void save(String title, Set<Long> authorIds, Set<Long> genreIds) {
        em.getTransaction().begin();
        try {
            em.persist(Book.builder()
                    .title(title)
                    .authors(new HashSet<>(authorDao.getByIds(new HashSet<>(authorIds))))
                    .genres(new HashSet<>(genreDao.getByIds(new HashSet<>(genreIds))))
                    .build());
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
