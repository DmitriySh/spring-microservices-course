package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.h2.tools.Console;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.domain.Genre;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.CommentRepository;
import ru.shishmakov.repository.GenreRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LibraryService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;
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
        return bookRepository.findAll().stream().map(Book::toString).collect(joining(lineSeparator()));
    }

    public String getAllAuthors() {
        return authorRepository.findAll().stream().map(Author::toString).collect(joining(lineSeparator()));
    }

    public String getAllGenres() {
        return genreRepository.findAll().stream().map(Genre::toString).collect(joining(lineSeparator()));
    }

    public String getAllComments() {
        return commentRepository.findAll().stream().map(Comment::toString).collect(joining(lineSeparator()));
    }

    public String getBookAuthors(long bookId) {
        Optional<Book> book = bookRepository.findByIdWithFetchAuthors(bookId);
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
        Optional<Book> book = bookRepository.findByIdWithFetchGenres(bookId);
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
        Optional<Book> book = bookRepository.findByIdWithFetchComments(bookId);
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
        List<Author> authors = authorRepository.findAllById(authorIds);
        List<Genre> genres = genreRepository.findAllById(genreIds);

        book.getAuthors().addAll(authors);
        book.getGenres().addAll(genres);
        bookRepository.save(book);
        return book.toString();
    }

    @Transactional
    public String createBookComment(long bookId, String commentText) {
        Comment comment = Comment.builder().text(commentText).createDate(Instant.now()).build();
        bookRepository.findById(bookId).ifPresent(b -> {
            b.addComment(comment);
            commentRepository.save(comment);
        });
        return comment.toString();
    }

    @Transactional
    public void deleteBook(long bookId) {
        bookRepository.findByIdWithFetchCommentsGenresAuthors(bookId).ifPresent(b -> {
            b.removeAllAuthors();
            b.removeAllGenres();
            b.removeAllComment();
            bookRepository.delete(b);
        });
    }

    @Transactional
    public void deleteComment(long commentId) {
        commentRepository.findByIdWithFetchBook(commentId).ifPresent(comment -> {
            comment.getBook().removeComment(comment);
            commentRepository.delete(comment);
        });
    }

    public void exit() {
        ofNullable(console).ifPresent(Console::shutdown);
        System.out.println(lineSeparator() + "\tGoodbye! =)" + lineSeparator());
        System.exit(0);
    }
}
