package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.shishmakov.dao.AuthorType;
import ru.shishmakov.dao.BookType;
import ru.shishmakov.dao.Dao;
import ru.shishmakov.dao.GenreType;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.EMPTY;


/**
 * User client
 */
@RequiredArgsConstructor
@ShellComponent
public class Library {
    @GenreType
    private final Dao<Genre> genreDao;
    @BookType
    private final Dao<Book> bookDao;
    @AuthorType
    private final Dao<Author> authorDao;
    private volatile Console console;

    @PostConstruct
    public void init() {
        System.out.print("\n\tWelcome to demo Library!\n\n");
    }

    @ShellMethod(value = "Get all books.", key = "get-books")
    public void getAllBooks() {
        System.out.println(bookDao.getAll().stream().map(Book::toString).collect(joining("\n")));
    }

    @ShellMethod(value = "Get all authors.", key = "get-authors")
    public void getAllAuthors() {
        System.out.println(authorDao.getAll().stream().map(Author::toString).collect(joining("\n")));
    }

    @ShellMethod(value = "Get all genres.", key = "get-genres")
    public void getAllGenres() {
        System.out.println(genreDao.getAll().stream().map(Genre::toString).collect(joining("\n")));
    }

    @ShellMethod(value = "Get authors of the book.")
    public void getBookAuthors(long bookId) {
        Book book = bookDao.getById(bookId);
        System.out.println("Book: " + book.getTitle());
        System.out.println("Authors:\n" + book.getAuthors().stream().map(Author::toString).collect(joining("\n")));
    }

    @ShellMethod(value = "Get genres of the book.")
    public void getBookGenres(long bookId) {
        Book book = bookDao.getById(bookId);
        System.out.println("Book: " + book.getTitle());
        System.out.println("Genres:\n" + book.getGenres().stream().map(Genre::toString).collect(joining("\n")));
    }

    @ShellMethod(value = "Create new book")
    public void createBook(String title, Set<Long> authorIds, List<Long> genreIds) {
        bookDao.save(Book.builder()
                .title(title)
                .authors(authorIds.stream().map(id -> new Author(id, EMPTY)).collect(toSet()))
                .genres(genreIds.stream().map(id -> new Genre(id, EMPTY)).collect(toSet()))
                .build());
    }

    @ShellMethod(value = ".")
    public void replaceBook(int number) {

    }

    @ShellMethod(value = "Run H2 database console.")
    public void h2() throws SQLException {
        if (console == null) {
            console = new Console();
            console.runTool();
        }
    }

    @ShellMethod(value = "Exit the library.", key = {"exit", "quit"})
    public void exit() {
        ofNullable(console).ifPresent(Console::shutdown);
        System.out.println("\n\tGoodbye! =)\n");
        System.exit(0);
    }

}
