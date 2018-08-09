package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.*;
import java.util.Map.Entry;

@Repository
public class BookRepository implements IRepository<Book> {

    private EntityManager em;

    @PersistenceUnit
    public void setEm(EntityManagerFactory em) {
        this.em = em.createEntityManager();
    }

    @Override
    public void save(Book book) {
        em.getTransaction().begin();
        try {
            em.persist(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
//        transaction.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus status) {
//                KeyHolder keyHolder = new GeneratedKeyHolder();
//                jdbcParameter.update("insert into book (title) values (:title)",
//                        new MapSqlParameterSource("title", book.getTitle()), keyHolder);
//                book.setId(requireNonNull(keyHolder.getKey()).longValue());
//
//                jdbcParameter.batchUpdate("insert into book_author (book_id, author_id) values (:book_id, :author_id)",
//                        createBatch(book.getAuthors().stream()
//                                .map(a -> new MapSqlParameterSource("book_id", book.getId())
//                                        .addValue("author_id", a.getId())
//                                        .getValues())
//                                .toArray()));
//
//                jdbcParameter.batchUpdate("insert into book_genre (book_id, genre_id) values (:book_id, :genre_id)",
//                        createBatch(book.getGenres().stream()
//                                .map(g -> new MapSqlParameterSource("book_id", book.getId())
//                                        .addValue("genre_id", g.getId())
//                                        .getValues())
//                                .toArray()));
//            }
//        });
    }

    @Override
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
//        transaction.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus status) {
//                jdbcParameter.update("delete from book_author where book_id = :book_id", new MapSqlParameterSource("book_id", bookId));
//                jdbcParameter.update("delete from book_genre where book_id = :book_id", new MapSqlParameterSource("book_id", bookId));
//                jdbcParameter.update("delete from book where id = :book_id", new MapSqlParameterSource("book_id", bookId));
//            }
//        });
    }

    @Override
    public Optional<Book> getById(long bookId, Map<String, Object> context) {
        // https://habr.com/post/265061/
        // https://www.thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
        // LEFT JOIN FETCH
        // methods for concrete fetch fields?
//        EntityGraph graph = this.em.getEntityGraph("graph.Book.authors.genres");
        EntityGraph<Book> graph = em.createEntityGraph(Book.class);
        context.entrySet().stream()
                .filter(e -> Objects.equals(e.getKey(), "eager"))
                .map(Entry::getValue)
                .map(List.class::cast)
                .flatMap(Collection::stream)
                .map(String::valueOf)
                .forEach(graph::addSubgraph);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        return Optional.ofNullable(em.find(Book.class, bookId, hints));
//        return requireNonNull(jdbcParameter.query(
//                "select b.id as book_id, b.title as book_title, ba.author_id as author_id, a.fullname as author_fullname, bg.genre_id as genre_id, g.name as genre_name " +
//                        " from " +
//                        " (select * from book where book.id = :book_id ) as b " +
//                        " left join book_author as ba " +
//                        " on b.id = ba.book_id " +
//                        "   left join book_genre as bg " +
//                        "   on b.id = bg.book_id " +
//                        " left join author as a " +
//                        " on a.id = ba.author_id " +
//                        "   left join genre as g " +
//                        "   on g.id = bg.genre_id ",
//                new MapSqlParameterSource("book_id", bookId),
//                mapBook()));
    }

    @Override
    public Collection<Book> getAll() {
        return em.createQuery("select b from Book b", Book.class) // lazy
                .getResultList();
//        return requireNonNull(jdbc.query(
//                "select b.id as book_id, b.title as book_title, ba.author_id as author_id, a.fullname as author_fullname, bg.genre_id as genre_id, g.name as genre_name " +
//                        " from book as b " +
//                        "   left join book_author as ba " +
//                        "   on b.id = ba.book_id " +
//                        " left join book_genre as bg " +
//                        " on b.id = bg.book_id " +
//                        "   left join author as a " +
//                        "   on a.id = ba.author_id " +
//                        " left join genre as g " +
//                        " on g.id = bg.genre_id"
//                , mapBooks()));
    }
}
