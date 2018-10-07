package ru.shishmakov.repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Book;

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

    @Test
    public void getAllShouldGetAllBooks() {
        List<Book> books = bookRepository.findAll();

        assertThat(books)
                .isNotNull()
                .hasSize(4)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdShouldGetBook() {
        Optional<Book> book = bookRepository.findById(1L);

        assertThat(book)
                .isNotNull()
                .isPresent()
                .get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Transactional
    public void saveAndFlushShouldSaveNewBook() {
        Book newBook = Book.builder().title("title").isbn("isbn").build();
        assertThat(newBook.getId())
                .isNull();

        Book book = bookRepository.saveAndFlush(newBook);
        assertThat(book.getId())
                .isNotNull()
                .isPositive();
    }

    @Test
    public void deleteShouldDeleteBook() {
        Book book = bookRepository.saveAndFlush(Book.builder().title("title").isbn("isbn").build());

        Long bookId = requireNonNull(book.getId());
        bookRepository.deleteById(bookId);

        Optional<Book> deletedBook = bookRepository.findById(bookId);
        assertThat(deletedBook)
                .isNotNull()
                .isNotPresent();
    }

    @Test
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
