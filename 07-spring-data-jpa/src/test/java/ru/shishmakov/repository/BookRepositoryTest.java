package ru.shishmakov.repository;

import org.assertj.core.util.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.domain.Genre;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class BookRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getAllShouldGetAllBooks() {
        List<Book> authors = bookRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(4)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdShouldGetBook() {
        Optional<Book> comment = bookRepository.findById(1L);

        assertThat(comment)
                .isNotNull()
                .isPresent()
                .get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Transactional
    public void saveAndFlushShouldSaveNewBook() {
        List<Author> authors = requireNonNull(authorRepository.findAllById(Sets.newLinkedHashSet(1L, 2L)));
        List<Genre> genres = requireNonNull(genreRepository.findAllById(Sets.newLinkedHashSet(1L, 2L)));
        Comment comment = Comment.builder().createDate(Instant.now()).text("next comment").build();
        Book newBook = Book.builder().title("title").isbn("isbn").build();

        newBook.getAuthors().addAll(authors);
        newBook.getGenres().addAll(genres);
        newBook.addComment(comment);
        Book book = bookRepository.saveAndFlush(newBook);

        assertThat(book.getId())
                .isNotNull()
                .isPositive();
        assertThat(book.getAuthors())
                .isNotEmpty()
                .containsAll(authors);
        assertThat(book.getGenres())
                .isNotEmpty()
                .containsAll(genres);
        assertThat(book.getComments())
                .isNotEmpty()
                .contains(comment);

        assertThat(comment.getId())
                .isNotNull()
                .isPositive();
        assertThat(comment.getBook())
                .isNotNull();
        assertThat(authors)
                .isNotNull()
                .allMatch(a -> a.getBooks().stream().anyMatch(b -> Objects.equals(b, book)));
        assertThat(genres)
                .isNotNull()
                .allMatch(g -> g.getBooks().stream().anyMatch(b -> Objects.equals(b, book)));
    }

    @Test
    @Transactional
    public void deleteShouldDeleteBook() {
        List<Author> authors = requireNonNull(authorRepository.findAllById(Sets.newLinkedHashSet(1L, 2L)));
        List<Genre> genres = requireNonNull(genreRepository.findAllById(Sets.newLinkedHashSet(1L, 2L)));
        Comment comment = Comment.builder().createDate(Instant.now()).text("next comment").build();
        Book newBook = Book.builder().title("title").isbn("isbn").build();

        newBook.getAuthors().addAll(authors);
        newBook.getGenres().addAll(genres);
        newBook.addComment(comment);
        Book book = bookRepository.saveAndFlush(newBook);

        Long bookId = requireNonNull(book.getId());
        Long commentId = requireNonNull(comment.getId());

        book.removeAllAuthors();
        book.removeAllGenres();
        book.removeAllComment();
        bookRepository.deleteById(bookId);

        Optional<Book> deletedBook = bookRepository.findById(bookId);

        assertThat(deletedBook)
                .isNotNull()
                .isNotPresent();
        assertThat(book.getAuthors())
                .isNotNull()
                .doesNotContain(authors.toArray(new Author[0]));
        assertThat(book.getGenres())
                .isNotNull()
                .doesNotContain(genres.toArray(new Genre[0]));
        assertThat(book.getComments())
                .isEmpty();

        assertThat(comment.getBook())
                .isNull();
        assertThat(commentRepository.findById(commentId))
                .isNotNull()
                .isNotPresent();
        assertThat(authors)
                .isNotNull()
                .allMatch(a -> a.getBooks().stream().noneMatch(b -> Objects.equals(b, book)));
        assertThat(genres)
                .isNotNull()
                .allMatch(g -> g.getBooks().stream().noneMatch(b -> Objects.equals(b, book)));
    }

    @Test
    @Transactional
    public void createBookShouldThrowExceptionIfTitleNull() {
        assertThatThrownBy(() -> bookRepository.saveAndFlush(Book.builder().title(null).isbn(UUID.randomUUID().toString()).build()))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement; SQL [n/a]; constraint [null]; nested exception");
    }

    @Test
    @Transactional
    public void createBookShouldThrowExceptionIfIsbnIsNotUnique() {
        assertThatThrownBy(() -> {
            bookRepository.saveAndFlush(Book.builder().title("title").isbn("not unique isbn").build());
            bookRepository.saveAndFlush(Book.builder().title("title").isbn("not unique isbn").build());
        })
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement; SQL [n/a]; constraint");
    }
}
