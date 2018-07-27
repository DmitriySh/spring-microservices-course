package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class BookDao implements Dao<Book> {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations jdbcParameter;

    @Override
    public void save(Book genre) {

    }

    @Override
    public void update(Book genre) {

    }

    @Override
    public void delete(Book genre) {

    }

    @Override
    public Book getById(long id) {
        return Objects.requireNonNull(jdbcParameter.query(
                "select b.id as book_id, b.title as book_title, ba.author_id as author_id, a.fullname as author_fullname, bg.genre_id as genre_id, g.name as genre_name " +
                        " from " +
                        " (select * from book where book.id = :book_id ) as b " +
                        " left join book_author as ba " +
                        " on b.id = ba.book_id " +
                        "   left join book_genre as bg " +
                        "   on b.id = bg.book_id " +
                        " left join author as a " +
                        " on a.id = ba.author_id " +
                        "   left join genre as g " +
                        "   on g.id = bg.genre_id ",
                Collections.singletonMap("book_id", id),
                mapBook()));
    }

    @Override
    public Collection<Book> getAll() {
        return Objects.requireNonNull(jdbc.query(
                "select b.id as book_id, b.title as book_title, ba.author_id as author_id, a.fullname as author_fullname, bg.genre_id as genre_id, g.name as genre_name " +
                        " from book as b " +
                        "   left join book_author as ba " +
                        "   on b.id = ba.book_id " +
                        " left join book_genre as bg " +
                        " on b.id = bg.book_id " +
                        "   left join author as a " +
                        "   on a.id = ba.author_id " +
                        " left join genre as g " +
                        " on g.id = bg.genre_id"
                , mapBooks()))
                .values();
    }

    private ResultSetExtractor<Book> mapBook() {
        return rs -> {
            Book result = new Book();
            result.setGenres(new HashSet<>());
            result.setAuthors(new HashSet<>());
            while (rs.next()) {
                if (isNull(result.getId())) result.setId(rs.getLong("book_id"));
                if (isNull(result.getTitle())) result.setTitle(rs.getString("book_title"));
                long authorId = rs.getLong("author_id");
                long genreId = rs.getLong("genre_id");
                String authorFullname = rs.getString("author_fullname");
                String genreName = rs.getString("genre_name");
                result.getGenres().add(new Genre(genreId, genreName));
                result.getAuthors().add(new Author(authorId, authorFullname));
            }
            return result;
        };
    }

    private ResultSetExtractor<Map<Long, Book>> mapBooks() {
        return rs -> {
            final Map<Long, Book> results = new HashMap<>();
            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                String bookTitle = rs.getString("book_title");
                long authorId = rs.getLong("author_id");
                long genreId = rs.getLong("genre_id");
                String authorFullname = rs.getString("author_fullname");
                String genreName = rs.getString("genre_name");
                results.compute(bookId, (id, book) -> ofNullable(book)
                        .map(b -> {
                            b.getAuthors().add(new Author(authorId, authorFullname));
                            b.getGenres().add(new Genre(genreId, genreName));
                            return b;
                        })
                        .orElseGet(() -> Book.builder()
                                .id(bookId)
                                .title(bookTitle)
                                .authors(ofNullable(authorFullname)
                                        .map(af -> new HashSet<>(singletonList(new Author(authorId, af))))
                                        .orElseGet(HashSet::new))
                                .genres(ofNullable(genreName)
                                        .map(gn -> new HashSet<>(singletonList(new Genre(genreId, gn))))
                                        .orElseGet(HashSet::new))
                                .build()));
            }
            return results;
        };
    }

}
