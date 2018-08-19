package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.h2.tools.Console;
import org.springframework.stereotype.Service;
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
        return bookDao.findAll().stream().map(Book::toString).collect(joining(lineSeparator()));
    }

    public String getAllAuthors() {
        return authorDao.findAll().stream().map(Author::toString).collect(joining(lineSeparator()));
    }

    public String getAllGenres() {
        return genreDao.findAll().stream().map(Genre::toString).collect(joining(lineSeparator()));
    }

    public String getAllComments() {
        return commentDao.findAll().stream().map(Comment::toString).collect(joining(lineSeparator()));
    }

    public String getBookAuthors(long bookId) {
        Optional<Book> book = bookDao.findByIdWithFetchAuthors(bookId);
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
        Optional<Book> book = bookDao.findByIdWithFetchGenres(bookId);
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
        Optional<Book> book = bookDao.findByIdWithFetchComments(bookId);
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

    public String createBook(String title, String isbn, Set<Long> authorIds, Set<Long> genreIds) {
        Book book = Book.builder().title(title).isbn(isbn).build();
        List<Author> authors = authorDao.findAllById(authorIds);
        List<Genre> genres = genreDao.findAllById(genreIds);

        book.getAuthors().addAll(authors);
        book.getGenres().addAll(genres);
        bookDao.save(book);
//            em.persist(book); // save entity
//            book.getAuthors().addAll(authors);
//            book.getGenres().addAll(genres);
//            em.merge(book); // save references
        return book.toString();
    }

    public String createBookComment(long bookId, String commentText) {
        Comment comment = Comment.builder().text(commentText).createDate(Instant.now()).build();
        bookDao.findById(bookId)
                .ifPresent(b -> {
                    comment.setBook(b);
                    commentDao.save(comment);
//                    comment.setBook(b);
//                    commentDao.save(comment);
//                    b.addComment(comment); // performance: update context if 'comment' don't have cascade updates
                });
        return comment.toString();
    }

    public void deleteBook(long bookId) {
        bookDao.deleteById(bookId);
//            getById(bookId, singletonMap("eager", singletonList("comments"))).ifPresent(b -> {
//                b.removeAllComment();
//                em.remove(b);
//            });
    }

    public void deleteComment(long commentId) {
        commentDao.findByIdWithFetchBook(commentId)
                .ifPresent(comment -> {
                    commentDao.delete(comment);
//                    Book book = comment.getBook();
//                    commentDao.delete(comment);
//                    book.removeComment(comment);
                });
    }

    public void exit() {
        ofNullable(console).ifPresent(Console::shutdown);
        System.out.println(lineSeparator() + "\tGoodbye! =)" + lineSeparator());
        System.exit(0);
    }
}
