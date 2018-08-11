package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.Set;

import static java.lang.System.lineSeparator;


/**
 * User client
 */
@RequiredArgsConstructor
@ShellComponent
public class LibraryShell {
    private final LibraryService service;

    @PostConstruct
    public void init() {
        System.out.println(lineSeparator() + "\tWelcome to demo Library!" + lineSeparator());
    }

    @ShellMethod(value = "Get all books.", key = "get-books")
    public String getAllBooks() {
        return service.getAllBooks();
    }

    @ShellMethod(value = "Get all authors.", key = "get-authors")
    public String getAllAuthors() {
        return service.getAllAuthors();
    }

    @ShellMethod(value = "Get all genres.", key = "get-genres")
    public String getAllGenres() {
        return service.getAllGenres();
    }

    @ShellMethod(value = "Get all comments.", key = "get-comments")
    public String getAllComments() {
        return service.getAllComments();
    }

    @ShellMethod(value = "Get authors of the book.")
    public String getBookAuthors(long bookId) {
        return service.getBookAuthors(bookId);
    }

    @ShellMethod(value = "Get genres of the book.")
    public String getBookGenres(long bookId) {
        return service.getBookGenres(bookId);
    }

    @ShellMethod(value = "Get comments of the book.")
    public String getBookComments(long bookId) {
        return service.getBookComments(bookId);
    }

    @ShellMethod(value = "Create new book.")
    public String createBook(String title, String isbn, Set<Long> authorIds, Set<Long> genreIds) {
        return service.createBook(title, isbn, authorIds, genreIds);
    }

    @ShellMethod(value = "Create new comment to book.")
    public String createBookComment(long bookId, String comment) {
        return service.createBookComment(bookId, comment);
    }

    @ShellMethod(value = "Delete the book.")
    public String deleteBook(long bookId) {
        service.deleteBook(bookId);
        return "deleted";
    }

    @ShellMethod(value = "Run H2 database console.")
    public String h2() throws SQLException {
        String status = "runnable";
        if (!service.hasConsole()) {
            service.initConsole();
            status = "running";
        }
        return status;
    }

    @ShellMethod(value = "Exit the library.", key = {"exit", "quit"})
    public void exit() {
        service.exit();
    }

}
