package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.h2.tools.Console;
import org.springframework.stereotype.Service;
import ru.shishmakov.dao.AuthorType;
import ru.shishmakov.dao.BookType;
import ru.shishmakov.dao.Dao;
import ru.shishmakov.dao.GenreType;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import java.sql.SQLException;
import java.util.Collection;

import static java.lang.System.lineSeparator;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@RequiredArgsConstructor
@Service
public class LibraryService {
    @GenreType
    private final Dao<Genre> genreDao;
    @BookType
    private final Dao<Book> bookDao;
    @AuthorType
    private final Dao<Author> authorDao;
    @Setter
    private volatile Console console;

    public boolean hasConsole() {
        return console != null;
    }

    public void initConsole() throws SQLException {
        Console console = new Console();
        console.runTool();
        this.console = console;
    }

    public String getAllBooks() {
        return bookDao.getAll().stream().map(Book::toString).collect(joining(lineSeparator()));
    }

    public String getAllAuthors() {
        return authorDao.getAll().stream().map(Author::toString).collect(joining(lineSeparator()));
    }

    public String getAllGenres() {
        return genreDao.getAll().stream().map(Genre::toString).collect(joining(lineSeparator()));
    }

    public String getBookAuthors(long bookId) {
        Book book = bookDao.getById(bookId);
        return new StringBuilder("Book: " + book.getTitle())
                .append(lineSeparator())
                .append("Authors:")
                .append(lineSeparator())
                .append(book.getAuthors().stream().map(Author::toString).collect(joining(lineSeparator())))
                .toString();

    }

    public String getBookGenres(long bookId) {
        Book book = bookDao.getById(bookId);
        return new StringBuilder("Book: " + book.getTitle())
                .append(lineSeparator())
                .append("Genres:")
                .append(lineSeparator())
                .append(book.getGenres().stream().map(Genre::toString).collect(joining(lineSeparator())))
                .toString();
    }

    public void createBook(String title, Collection<Long> authorIds, Collection<Long> genreIds) {
        bookDao.save(Book.builder()
                .title(title)
                .authors(authorIds.stream().distinct().map(id -> new Author(id, EMPTY)).collect(toSet()))
                .genres(genreIds.stream().distinct().map(id -> new Genre(id, EMPTY)).collect(toSet()))
                .build());
    }

    public void deleteBook(long bookId) {
        bookDao.delete(bookId);
    }

    public void exit() {
        ofNullable(console).ifPresent(Console::shutdown);
        System.out.println(lineSeparator() + "\tGoodbye! =)" + lineSeparator());
        System.exit(0);
    }
}
