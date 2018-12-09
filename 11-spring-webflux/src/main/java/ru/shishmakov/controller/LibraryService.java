package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.GenreRepository;
import ru.shishmakov.web.BookDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibraryService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Value("${limit:10000}")
    private int limit;

    public Flux<Book> getAllBooks() {
        return bookRepository.findAll()
                .limitRequest(limit)
                .doFinally(signal -> log.info("get all book items"));
    }

    public Mono<Book> getBookById(ObjectId bookId) {
        return bookRepository.findById(bookId)
                .doFinally(signal -> log.info("get book by id: " + bookId));
    }

//    public String getAllAuthors() {
//        return authorRepository.findAll().stream().map(Author::toString).collect(joining(lineSeparator()));
//    }

//    public String getAllGenres() {
//        return genreRepository.findAll().stream().map(Genre::toString).collect(joining(lineSeparator()));
//    }

//    public String getAllComments() {
//        StringBuilder builder = new StringBuilder();
//        bookRepository.findAllWithFetchComments().forEach(b ->
//                builder.append("Book:")
//                        .append(lineSeparator())
//                        .append(b.toString())
//                        .append(lineSeparator())
//                        .append("Comments:")
//                        .append(lineSeparator())
//                        .append(b.getComments().stream().map(Comment::toString).collect(joining(lineSeparator())))
//                        .append(lineSeparator()));
//        return builder.toString();
//    }

//    public String getBookAuthors(ObjectId bookId) {
//        Optional<Book> book = bookRepository.findByIdWithFetchAuthors(bookId);
//        return book.map(b -> new StringBuilder("Book:")
//                .append(lineSeparator())
//                .append(b.toString())
//                .append(lineSeparator())
//                .append("Authors:")
//                .append(lineSeparator())
//                .append(b.getAuthors().stream().map(Author::toString).collect(joining(lineSeparator())))
//                .toString())
//                .orElseGet(() -> "book: " + bookId + " not found");
//
//    }

//    public String getBookGenres(ObjectId bookId) {
//        Optional<Book> book = bookRepository.findByIdWithFetchGenres(bookId);
//        return book.map(b -> new StringBuilder("Book:")
//                .append(lineSeparator())
//                .append(b.toString())
//                .append(lineSeparator())
//                .append("Genres:")
//                .append(lineSeparator())
//                .append(b.getGenres().stream().map(Genre::toString).collect(joining(lineSeparator())))
//                .toString())
//                .orElseGet(() -> "book: " + bookId + " not found");
//    }

//    public String getBookComments(ObjectId bookId) {
//        Optional<Book> book = bookRepository.findByIdWithFetchComments(bookId);
//        return book.map(b -> new StringBuilder("Book:")
//                .append(lineSeparator())
//                .append(b.toString())
//                .append(lineSeparator())
//                .append("Comments:")
//                .append(lineSeparator())
//                .append(b.getComments().stream().map(Comment::toString).collect(joining(lineSeparator())))
//                .toString())
//                .orElseGet(() -> "book: " + bookId + " not found");
//    }

    public Mono<Book> createBook(BookDto data) {
        Flux<Author> authors = authorRepository.findAllById(data.getAuthors());
        Flux<Genre> genres = genreRepository.findAllById(data.getGenres());
        return Flux.just(Book.builder().title(data.getTitle()).isbn(data.getIsbn()).build())
                .zipWith(authors, 3, Book::addAuthor)
                .zipWith(genres, 3, Book::addGenre)
                .flatMap(bookRepository::save)
                .doOnComplete(() -> authorRepository.saveAll(authors))
                .doOnComplete(() -> genreRepository.saveAll(genres))
                .doFinally(signal -> log.info("save book: {}"))
                .next();
    }

//    public Mono<Book> updateBook(BookDto data) {
//        return bookRepository.findById(data.getId())
//                .flatMap(book -> {
//                    Set<Author> oldAuthors = book.getAuthors();
//                    Flux<Author> newAuthors = authorRepository.findAllByIdWithFetchBooks(data.getAuthors());
//                    newAuthors
//                            .doOnNext(author -> {
//                                if (!oldAuthors.contains(author)) {
//
//                                }
//                                author
//                            })
//                            .doOnNext(author -> author.getBooks().add(book))
//
//
//                    book.setTitle(data.getTitle());
//                    book.setIsbn(data.getIsbn());
//                    book.setAuthors(newAuthors);
//                    book.setGenres(newGenres);
//                    return bookRepository.save(book);
//                });
//    }

//    public String createBookComment(ObjectId bookId, String commentText) {
//        return bookRepository.findById(bookId)
//                .map(b -> {
//                    Comment comment = Comment.builder().id(new ObjectId()).text(commentText).createDate(Instant.now()).build();
//                    b.addComment(comment);
//                    bookRepository.save(b);
//                    return comment.toString();
//                })
//                .orElseGet(() -> "book: " + bookId + " not found");
//    }

//    public Mono<Void> deleteBook(ObjectId bookId) {
//        return bookRepository.findByIdWithFetchGenresAuthors(bookId)
//                .map(b -> {
//                    Set<Author> authors = b.removeAllAuthors();
//                    Set<Genre> genres = b.removeAllGenres();
//                    authorRepository.saveAll(authors);
//                    genreRepository.saveAll(genres);
//                    bookRepository.delete(b);
//                    return "deleted";
//                })
//                .orElseGet(() -> "book: " + bookId + " not found");
//    }

//    public String deleteComment(ObjectId bookId, ObjectId commentId) {
//        return bookRepository.findById(bookId)
//                .map(b -> {
//                    String response = b.removeComment(commentId) ? "deleted" : "comment: " + commentId + " not found";
//                    bookRepository.save(b);
//                    return response;
//                })
//                .orElseGet(() -> "book: " + bookId + " not found");
//    }
}
