package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@ShellComponent
public class Library {
    @GenreType
    private final Dao<Genre> genreDao;
    @BookType
    private final Dao<Book> bookDao;
    @AuthorType
    private final Dao<Author> authorDao;

    @PostConstruct
    public void init() {
        System.out.print("\n\tWelcome to demo Library!\n");
    }

    @ShellMethod(value = "Get all books.")
    public void getAllBooks() {
        System.out.println("All books: " + bookDao.getAll().stream().map(Book::toString).collect(joining(",")));
    }

    @ShellMethod(value = "Get all authors.")
    public void getAllAuthors() {
        System.out.println("All authors: " + authorDao.getAll().stream().map(Author::toString).collect(joining(",")));
    }

    @ShellMethod(value = "Get all genres.")
    public void getAllGenres() {
        System.out.println("All genres: " + genreDao.getAll().stream().map(Genre::toString).collect(joining(",")));
    }

    @ShellMethod(value = ".")
    public void getBookAuthors(int bookId) {
        Book book = bookDao.getById(bookId);
        System.out.println("Book: " + book.getTitle());
        System.out.println("Authors: " + book.getAuthors().stream().map(Author::toString).collect(joining(",")));
    }

    @ShellMethod(value = ".")
    public void getBookGenres(int bookId) {
        Book book = bookDao.getById(bookId);
        System.out.println("Book: " + book.getTitle());
        System.out.println("Genres: " + book.getGenres().stream().map(Genre::toString).collect(joining(",")));
    }

    @ShellMethod(value = ".")
    public void createBook(String title, List<Integer> authors, List<String> genres) {
        List<Author> authorList = null;
        List<Genre> genreList = null;
        Book book = new Book(/*???*/null, title, authorList, genreList);
        bookDao.save(book);
        // log
    }

    @ShellMethod(value = ".")
    public void replaceBook(int number) {

    }


}
