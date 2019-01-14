package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.shishmakov.persistence.entity.Author;
import ru.shishmakov.persistence.entity.Book;
import ru.shishmakov.persistence.entity.Comment;
import ru.shishmakov.persistence.entity.Genre;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class BookRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    private List<Book> dbBooks;

    @Before
    public void setUp() {
        List<Author> authors = List.of(
                Author.builder().fullname("author 1").build(),
                Author.builder().fullname("author 2").build(),
                Author.builder().fullname("author 3").build(),
                Author.builder().fullname("author 4").build());
        List<Genre> genres = List.of(
                Genre.builder().name("genre 1").build(),
                Genre.builder().name("genre 2").build(),
                Genre.builder().name("genre 3").build());

        authorRepository.deleteAll()
                .thenMany(Flux.fromIterable(authors)
                        .collectList()
                        .flatMapMany(authorRepository::saveAll))
                .thenMany(authorRepository.findAll())
                .collectList()
                .block();
        genreRepository.deleteAll()
                .thenMany(Flux.fromIterable(genres)
                        .collectList()
                        .flatMapMany(genreRepository::saveAll))
                .thenMany(genreRepository.findAll())
                .collectList()
                .block();

        dbBooks = List.of(Book.builder().title("book 1").isbn("0-395-08254-1").comments(buildComments(3)).build()
                        .addAuthors(filter(authors, Author::getFullname, "author 1", "author 2", "author 3", "author 4"))
                        .addGenres(filter(genres, Genre::getName, "genre 1", "genre 2")),
                Book.builder().title("book 2").isbn("0-395-08254-2").comments(buildComments(2)).build()
                        .addAuthors(filter(authors, Author::getFullname, "author 1", "author 3"))
                        .addGenres(filter(genres, Genre::getName, "genre 2")),
                Book.builder().title("book 3").isbn("0-395-08254-3").build(),
                Book.builder().title("book 4").isbn("0-395-08254-4").comments(buildComments(1)).build()
                        .addAuthors(filter(authors, Author::getFullname, "author 4"))
                        .addGenres(filter(genres, Genre::getName, "genre 1")));

        bookRepository.deleteAll()
                .thenMany(bookRepository.saveAll(dbBooks))
                .thenMany(bookRepository.findAll())
                .collectList()
                .thenMany(authorRepository.saveAll(authors))
                .thenMany(genreRepository.saveAll(genres))
                .then().block();
    }

    @Test
    public void getAllShouldGetAllBooks() {
        Stream<Book> books = bookRepository.findAll().toStream();

        assertThat(books)
                .isNotNull()
                .hasSize(dbBooks.size())
                .allMatch(Objects::nonNull, "all elements are not null")
                .allMatch(book -> Objects.nonNull(book.getId()), "all ids are initialized");
    }

    @Test
    public void findByIdShouldNotGetBookIfNotAvailable() {
        ObjectId absentBookId = new ObjectId();
        Mono<Book> book = bookRepository.findById(absentBookId);

        StepVerifier
                .create(book)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void findAllByIdShouldGetBooksIfTheyAvailable() {
        Set<ObjectId> bookIds = dbBooks.stream().map(Book::getId).collect(toSet());
        Flux<Book> books = bookRepository.findAllById(bookIds);

        StepVerifier
                .create(books)
                .expectNextCount(dbBooks.size())
                .verifyComplete();
    }

    @Test
    public void findAllByIdShouldNotGetBooksIfTheyNotAvailable() {
        Set<ObjectId> absentBookIds = Set.of(new ObjectId(), new ObjectId());
        Flux<Book> books = bookRepository.findAllById(absentBookIds);

        StepVerifier
                .create(books)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteByIdShouldDeleteBook() {
        ObjectId bookId = dbBooks.get(0).getId();
        Mono<Book> book = bookRepository.findById(bookId);
        StepVerifier
                .create(book)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Book> deletedBook = bookRepository.deleteById(bookId)
                .then(bookRepository.findById(bookId));
        StepVerifier
                .create(deletedBook)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteAllShouldDeleteAllBooks() {
        Flux<Book> booksBefore = bookRepository.findAll();
        StepVerifier
                .create(booksBefore)
                .expectNextCount(dbBooks.size())
                .verifyComplete();

        Flux<Book> booksAfter = bookRepository
                .deleteAll()
                .thenMany(bookRepository.findAll());
        StepVerifier
                .create(booksAfter)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void saveBookShouldSucceeded() {
        Mono<Book> savedGenre = bookRepository.save(Book.builder().isbn("isbn").title("title").build());

        StepVerifier
                .create(savedGenre)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveBookShouldThrowExceptionIfIsbnNull() {
        Mono<Book> savedBook = bookRepository.save(Book.builder().isbn(null).title("title").build());

        StepVerifier
                .create(savedBook)
                .expectNextCount(0)
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(ConstraintViolationException.class)
                .hasOperatorErrorWithMessage("isbn: must not be empty");
    }

    private Set<Comment> buildComments(int count) {
        return LongStream.range(0, count)
                .map(id -> id + 1)
                .mapToObj(id -> Comment.builder().id(new ObjectId())
                        .createDate(Instant.now())
                        .text("comment " + id)
                        .build())
                .collect(toSet());
    }

    private <T> Set<T> filter(List<T> src, Function<T, String> function, String... items) {
        Set<String> set = Set.of(items);
        return src.stream()
                .filter(a -> set.contains(function.apply(a)))
                .collect(toSet());
    }
}
