package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shishmakov.persistence.entity.Author;
import ru.shishmakov.persistence.entity.Book;
import ru.shishmakov.persistence.entity.Genre;
import ru.shishmakov.persistence.repository.AuthorRepository;
import ru.shishmakov.persistence.repository.BookRepository;
import ru.shishmakov.persistence.repository.GenreRepository;
import ru.shishmakov.web.dto.BookDto;

import java.util.Set;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibraryService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Flux<Book> getAllBooks(int limit) {
        return bookRepository.findAllWithFetchGenresAuthors()
                .limitRequest(limit)
                .doFinally(signal -> log.info("get all book items"));
    }

    public Mono<Book> getBookById(ObjectId bookId) {
        return bookRepository.findByIdWithFetchGenresAuthors(bookId)
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
                .zipWith(authors, 2, Book::addAuthor)
                .zipWith(genres, 2, Book::addGenre)
                .flatMap(bookRepository::save)
                .doOnComplete(() -> authorRepository.saveAll(authors))
                .doOnComplete(() -> genreRepository.saveAll(genres))
                .doFinally(signal -> log.info("save book"))
                .single();
    }

    public Mono<Book> updateBook(BookDto data) {
        return bookRepository.findById(data.getId())
                /*новые авторы*/
                .flatMap(book -> processNewAuthors(data, book))
                /*новые жанры*/
                .flatMap(book -> processNewGenres(data, book))
                /*итоговое состояние книги*/
                .flatMap(book -> {
                    book.setIsbn(data.getIsbn());
                    book.setTitle(data.getTitle());
                    return bookRepository.save(book);
                });
    }

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

    public Mono<Void> deleteBook(ObjectId bookId) {
        return bookRepository.findByIdWithFetchGenresAuthors(bookId)
                .flatMap(book -> authorRepository.saveAll(book.removeAllAuthors())
                        .thenMany(genreRepository.saveAll(book.removeAllGenres()))
                        .then(Mono.just(book)))
                .flatMap(bookRepository::delete);
    }

//    public String deleteComment(ObjectId bookId, ObjectId commentId) {
//        return bookRepository.findById(bookId)
//                .map(b -> {
//                    String response = b.removeComment(commentId) ? "deleted" : "comment: " + commentId + " not found";
//                    bookRepository.save(b);
//                    return response;
//                })
//                .orElseGet(() -> "book: " + bookId + " not found");
//    }

    private Mono<Book> processNewGenres(BookDto data, Book book) {
        return ofNullable(data.getGenres())
                .map(genreRepository::findAllByIdWithFetchBooks)
                .orElseGet(Flux::empty)
                // оставляем только старые жанры в book
                .doOnNext(newGenre -> book.getGenres().remove(newGenre))
                // собираем все новые жанры
                .collectList()
                .flatMap(newGenres -> {
                    // в book остались только старые жанры:
                    // 1) убираем из книги старые жанры
                    // 2) из каждого старого жанра убираем книгу
                    Set<Genre> oldGenres = book.removeAllGenres();
                    // 3) добавляем в книгу новые жанры
                    book.addGenres(newGenres);
                    return Mono.just(oldGenres);
                })
                // состояние старых жанров нужно сохранить
                .flatMapMany(genreRepository::saveAll)
                .then(Mono.just(book));
    }

    private Mono<Book> processNewAuthors(BookDto data, Book book) {
        return ofNullable(data.getAuthors())
                .map(authorRepository::findAllByIdWithFetchBooks)
                .orElseGet(Flux::empty)
                // оставляем только старых авторов в book
                .doOnNext(newAuthor -> book.getAuthors().remove(newAuthor))
                // собираем всех новых авторов
                .collectList()
                .flatMap(newAuthors -> {
                    // в book остались только старые авторы:
                    // 1) убираем из книги старых авторов
                    // 2) из каждого старого автора убираем книгу
                    Set<Author> oldAuthors = book.removeAllAuthors();
                    // 3) добавляем в книгу новых авторов
                    book.addAuthors(newAuthors);
                    return Mono.just(oldAuthors);
                })
                // состояние старых книг нужно сохранить
                .flatMapMany(authorRepository::saveAll)
                .then(Mono.just(book));
    }

}
