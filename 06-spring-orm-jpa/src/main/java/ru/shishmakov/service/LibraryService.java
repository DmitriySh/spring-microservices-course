package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.h2.tools.Console;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.dao.AuthorRepository;
import ru.shishmakov.dao.BookRepository;
import ru.shishmakov.dao.CommentRepository;
import ru.shishmakov.dao.GenreRepository;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.domain.Genre;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LibraryService {
    private final GenreRepository genreDao;
    private final BookRepository bookDao;
    private final CommentRepository commentDao;
    private final AuthorRepository authorDao;
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

    public String getAllComments() {
        return commentDao.getAll().stream().map(Comment::toString).collect(joining(lineSeparator()));
    }

    public String getBookAuthors(long bookId) {
        Optional<Book> book = bookDao.getById(bookId, singletonMap("eager", singletonList("authors")));
        return book.map(b -> new StringBuilder("Book:")
                .append(lineSeparator())
                .append(b.toString())
                .append(lineSeparator())
                .append("Authors:")
                .append(lineSeparator())
                .append(b.getAuthors().stream().map(Author::toString).collect(joining(lineSeparator())))
                .toString())
                .orElseGet(() -> "book: " + bookId + " not found");

    }

    public String getBookGenres(long bookId) {
        Optional<Book> book = bookDao.getById(bookId, singletonMap("eager", singletonList("genres")));
        return book.map(b -> new StringBuilder("Book:")
                .append(lineSeparator())
                .append(b.toString())
                .append(lineSeparator())
                .append("Genres:")
                .append(lineSeparator())
                .append(b.getGenres().stream().map(Genre::toString).collect(joining(lineSeparator())))
                .toString())
                .orElseGet(() -> "book: " + bookId + " not found");
    }

    public String getBookComments(long bookId) {
        Optional<Book> book = bookDao.getById(bookId, singletonMap("eager", singletonList("comments")));
        return book.map(b -> new StringBuilder("Book:")
                .append(lineSeparator())
                .append(b.toString())
                .append(lineSeparator())
                .append("Comments:")
                .append(lineSeparator())
                .append(b.getComments().stream().map(Comment::toString).collect(joining(lineSeparator())))
                .toString())
                .orElseGet(() -> "book: " + bookId + " not found");
    }

    @Transactional
    public String createBook(String title, String isbn, Set<Long> authorIds, Set<Long> genreIds) {
        Book book = Book.builder().title(title).isbn(isbn).build();
        List<Author> authors = authorDao.getByIds(authorIds);
        List<Genre> genres = genreDao.getByIds(genreIds);
        bookDao.save(book, authors, genres);
        return book.toString();
    }

    @Transactional
    public String createBookComment(long bookId, String commentText) {
        return bookDao.getById(bookId, emptyMap())
                .map(b -> {
                    Comment comment = Comment.builder().text(commentText).createDate(Instant.now()).build();
                    commentDao.save(comment, b);
                    return comment.toString();
                })
                .orElseGet(() -> "book: " + bookId + " not found");
    }

    @Transactional
    public void deleteBook(long bookId) {
        bookDao.delete(bookId);
    }

    @Transactional
    public void deleteComment(long commentId) {
        commentDao.getById(commentId, singletonMap("eager", singletonList("book")))
                .ifPresent(commentDao::delete);
    }

    public void exit() {
        ofNullable(console).ifPresent(Console::shutdown);
        System.out.println(lineSeparator() + "\tGoodbye! =)" + lineSeparator());
        System.exit(0);
    }
}
