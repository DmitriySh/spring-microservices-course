package ru.shishmakov.repository;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.domain.Genre;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test MongoDb Data layer without Web
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
    private List<Author> dbAuthors;
    private List<Genre> dbGenres;
    private Set<Comment> dbComments;
    private List<Book> dbBooks;

    @Before
    public void setUp() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
        authorRepository.deleteAll();

        this.dbAuthors = requireNonNull(authorRepository.saveAll(Arrays.asList(
                Author.builder().fullname("author 1").build(),
                Author.builder().fullname("author 2").build())));

        this.dbGenres = requireNonNull(genreRepository.saveAll(Arrays.asList(
                Genre.builder().name("genre 1").build(),
                Genre.builder().name("genre 2").build())));

        this.dbComments = new HashSet<>(Arrays.asList(
                Comment.builder().id(new ObjectId()).createDate(Instant.now()).text("comment 1").build(),
                Comment.builder().id(new ObjectId()).createDate(Instant.now()).text("comment 2").build()
        ));

        this.dbBooks = requireNonNull(bookRepository.saveAll(Arrays.asList(
                Book.builder().title("book 1").isbn("0-395-08254-1").comments(dbComments).build()
                        .addAuthors(dbAuthors)
                        .addGenres(dbGenres),
                Book.builder().title("book 2").isbn("0-395-08254-2").comments(dbComments).build()
                        .addAuthors(dbAuthors)
                        .addGenres(dbGenres))));

        authorRepository.saveAll(dbAuthors);
        genreRepository.saveAll(dbGenres);
    }

    @Test
    public void findAllShouldGetAllBooks() {
        List<Book> books = bookRepository.findAll();

        assertThat(books)
                .isNotNull()
                .hasSameSizeAs(dbBooks)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void findByIdShouldGetBook() {
        Optional<Book> book = bookRepository.findById(dbBooks.get(0).get_id());

        assertThat(book)
                .isNotNull()
                .isPresent()
                .get().isIn(dbBooks);
    }

    @Test
    public void findByIdShouldNotGetBookIfNotAvailable() {
        Optional<Book> book = bookRepository.findById(new ObjectId());

        assertThat(book)
                .isNotNull()
                .isNotPresent();
    }

    @Test
    public void saveShouldSaveNewBook() {
        Book newBook = Book.builder().title("title").isbn("isbn").build();
        newBook.addAuthors(dbAuthors);
        newBook.addGenres(dbGenres);
        newBook.addComments(dbComments);

        Book book = bookRepository.save(newBook);
        authorRepository.saveAll(dbAuthors);
        genreRepository.saveAll(dbGenres);

        assertThat(book.get_id())
                .isNotNull();
        assertThat(book.getAuthors())
                .isNotEmpty()
                .containsAll(dbAuthors);
        assertThat(book.getGenres())
                .isNotEmpty()
                .containsAll(dbGenres);
        assertThat(book.getComments())
                .isNotEmpty()
                .containsAll(dbComments);

        assertThat(dbAuthors)
                .isNotNull()
                .allMatch(a -> a.getBooks().stream().anyMatch(b -> Objects.equals(b, book)));
        assertThat(dbGenres)
                .isNotNull()
                .allMatch(g -> g.getBooks().stream().anyMatch(b -> Objects.equals(b, book)));
    }

    @Test
    public void deleteShouldDeleteBook() {
        Book newBook = Book.builder().title("title").isbn("isbn").build();
        newBook.addAuthors(dbAuthors);
        newBook.addGenres(dbGenres);
        newBook.addComments(dbComments);

        Book book = bookRepository.save(newBook);
        ObjectId bookId = requireNonNull(book.get_id());

        book.removeAllAuthors();
        book.removeAllGenres();
        book.removeAllComments();
        bookRepository.deleteById(bookId);

        Optional<Book> deletedBook = bookRepository.findById(bookId);

        assertThat(deletedBook)
                .isNotNull()
                .isNotPresent();
        assertThat(book.getAuthors())
                .isNotNull()
                .doesNotContain(dbAuthors.toArray(new Author[0]));
        assertThat(book.getGenres())
                .isNotNull()
                .doesNotContain(dbGenres.toArray(new Genre[0]));
        assertThat(book.getComments())
                .isEmpty();

        assertThat(dbAuthors)
                .isNotNull()
                .allMatch(a -> a.getBooks().stream().noneMatch(b -> Objects.equals(b, book)));
        assertThat(dbGenres)
                .isNotNull()
                .allMatch(g -> g.getBooks().stream().noneMatch(b -> Objects.equals(b, book)));
    }

    @Test
    public void createBookShouldThrowExceptionIfTitleNull() {
        assertThatThrownBy(() -> bookRepository.save(Book.builder().title(null).isbn(UUID.randomUUID().toString()).build()))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("title:");
    }

    @Test
    public void createBookShouldThrowExceptionIfIsbnIsNotUnique() {
        assertThatThrownBy(() -> {
            bookRepository.save(Book.builder().title("title").isbn("not unique isbn").build());
            bookRepository.save(Book.builder().title("title").isbn("not unique isbn").build());
        })
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("library.book index: isbn dup key");
    }
}
