package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class BookDao implements Dao<Book> {
    private final JdbcOperations jdbc;

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
    public Book getById(int id) {
        return null;
    }

    @Override
    public Collection<Book> getAll() {
        Map<Long, Book> bookMap = jdbc.query(
                "select b.id as book_id, b.title as book_title, ba.author_id as author_id, a.fullname as author_fullname, bg.genre_id as genre_id, g.name as genre_name" +
                        " from book as b " +
                        "   left join book_author as ba " +
                        "   on b.id = ba.book_id " +
                        " left join book_genre as bg " +
                        " on b.id = bg.book_id " +
                        "   left join author as a " +
                        "   on a.id = ba.author_id " +
                        " left join genre as g " +
                        " on g.id = bg.genre_id;"
                , bookMap());
        return bookMap.values();
    }

    private ResultSetExtractor<Map<Long, Book>> bookMap() {
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
