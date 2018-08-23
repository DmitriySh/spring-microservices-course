package ru.shishmakov.dao;

import org.assertj.core.util.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @SpyBean
    private BookRepository bookRepository;
    @SpyBean
    private AuthorRepository authorRepository;
    @SpyBean
    private GenreRepository genreRepository;

    @Test
    public void getAllShouldGetAllBooks() {
        List<Book> authors = bookRepository.getAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(4)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdShouldGetBook() {
        Optional<Book> comment = bookRepository.getById(1L, emptyMap());

        assertThat(comment)
                .isNotNull()
                .isPresent()
                .get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    public void saveShouldSaveNewBook() throws InterruptedException {
        List<Author> authors = requireNonNull(authorRepository.getByIds(Sets.newLinkedHashSet(1L, 2L)));
        List<Genre> genres = requireNonNull(genreRepository.getByIds(Sets.newLinkedHashSet(1L, 2L)));
        Book book = Book.builder().title("title").isbn("isbn 1").build();
        bookRepository.save(book, authors, genres);

        assertThat(book.getId())
                .isNotNull()
                .isPositive();
        assertThat(book.getAuthors())
                .isNotNull()
                .containsAll(authors);
        assertThat(book.getGenres())
                .isNotNull()
                .containsAll(genres);
//        assertThat(authors)
//                .isNotNull()
//                .allMatch(a -> a.getBooks().stream().anyMatch(b -> Objects.equals(b, book)));
//        assertThat(genres)
//                .isNotNull()
//                .allMatch(g -> g.getBooks().stream().anyMatch(b -> Objects.equals(b, book)));
    }

    @Test
    public void deleteShouldDeleteBook() {
        List<Author> authors = requireNonNull(authorRepository.getByIds(Sets.newLinkedHashSet(1L, 2L)));
        List<Genre> genres = requireNonNull(genreRepository.getByIds(Sets.newLinkedHashSet(1L, 2L)));
        Book newBook = Book.builder().title("title").isbn("isbn 2").build();
        bookRepository.save(newBook, authors, genres);

        Long newBookId = requireNonNull(newBook.getId());

        bookRepository.delete(newBookId);
        Optional<Book> deletedBook = bookRepository.getById(newBookId, emptyMap());

        assertThat(deletedBook)
                .isNotNull()
                .isNotPresent();
        assertThat(authors)
                .isNotNull()
                .allMatch(a -> a.getBooks().stream().noneMatch(b -> Objects.equals(b, newBook)));
        assertThat(genres)
                .isNotNull()
                .allMatch(g -> g.getBooks().stream().noneMatch(b -> Objects.equals(b, newBook)));
    }
}
