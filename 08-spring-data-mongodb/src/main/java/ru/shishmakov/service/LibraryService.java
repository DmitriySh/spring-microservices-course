package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.domain.Genre;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.GenreRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@Service
public class LibraryService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

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
        StringBuilder builder = new StringBuilder();
        bookRepository.findAllWithFetchComments().forEach(b ->
                builder.append("Book:")
                        .append(lineSeparator())
                        .append(b.toString())
                        .append(lineSeparator())
                        .append("Comments:")
                        .append(lineSeparator())
                        .append(b.getComments().stream().map(Comment::toString).collect(joining(lineSeparator())))
                        .append(lineSeparator()));
        return builder.toString();
    }

    public String getBookAuthors(ObjectId bookId) {
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

    public String getBookGenres(ObjectId bookId) {
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

    public String getBookComments(ObjectId bookId) {
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

    public String createBook(String title, String isbn, Set<ObjectId> authorIds, Set<ObjectId> genreIds) {
        Book book = Book.builder().title(title).isbn(isbn).build();

        List<Author> authors = authorRepository.findAllById(authorIds);
        List<Genre> genres = genreRepository.findAllById(genreIds);

        book.addAuthors(authors);
        book.addGenres(genres);
        bookRepository.save(book);
        authorRepository.saveAll(authors);
        genreRepository.saveAll(genres);
        return book.toString();
    }

    public String createBookComment(ObjectId bookId, String commentText) {
        return bookRepository.findById(bookId)
                .map(b -> {
                    Comment comment = Comment.builder().id(new ObjectId()).text(commentText).createDate(Instant.now()).build();
                    b.addComment(comment);
                    bookRepository.save(b);
                    return comment.toString();
                })
                .orElseGet(() -> "book: " + bookId + " not found");
    }

    public String deleteBook(ObjectId bookId) {
        return bookRepository.findByIdWithFetchGenresAuthors(bookId)
                .map(b -> {
                    Set<Author> authors = b.removeAllAuthors();
                    Set<Genre> genres = b.removeAllGenres();
                    authorRepository.saveAll(authors);
                    genreRepository.saveAll(genres);
                    bookRepository.delete(b);
                    return "deleted";
                })
                .orElseGet(() -> "book: " + bookId + " not found");
    }

    public String deleteComment(ObjectId bookId, ObjectId commentId) {
        return bookRepository.findById(bookId)
                .map(b -> {
                    String response = b.removeComment(commentId) ? "deleted" : "comment: " + commentId + " not found";
                    bookRepository.save(b);
                    return response;
                })
                .orElseGet(() -> "book: " + bookId + " not found");
    }

    public void exit() {
        System.out.println(lineSeparator() + "\tGoodbye! =)" + lineSeparator());
        System.exit(0);
    }
}
