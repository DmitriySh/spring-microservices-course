package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.PostConstruct;
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
    public String getBookAuthors(ObjectId bookId) {
        return service.getBookAuthors(bookId);
    }

    @ShellMethod(value = "Get genres of the book.")
    public String getBookGenres(ObjectId bookId) {
        return service.getBookGenres(bookId);
    }

    @ShellMethod(value = "Get comments of the book.")
    public String getBookComments(ObjectId bookId) {
        return service.getBookComments(bookId);
    }

    @ShellMethod(value = "Create new book.")
    public String createBook(String title, String isbn, Set<ObjectId> authorIds, Set<ObjectId> genreIds) {
        return service.createBook(title, isbn, authorIds, genreIds);
    }

    @ShellMethod(value = "Create new comment to book.")
    public String createBookComment(ObjectId bookId, String comment) {
        return service.createBookComment(bookId, comment);
    }

    @ShellMethod(value = "Delete the book.")
    public String deleteBook(ObjectId bookId) {
        return service.deleteBook(bookId);
    }

    @ShellMethod(value = "Delete the comment.")
    public String deleteBookComment(ObjectId bookId, ObjectId commentId) {
        return service.deleteComment(bookId, commentId);
    }

    @ShellMethod(value = "Exit the library.", key = {"exit", "quit"})
    public void exit() {
        service.exit();
    }
}
