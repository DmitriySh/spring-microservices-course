package ru.shishmakov.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.shishmakov.dao.BookType;
import ru.shishmakov.dao.Dao;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @Autowired
    private Library library;

    @MockBean
    private Shell shell;
    @SpyBean
    @BookType
    private Dao<Book> bookDao;
    @SpyBean
    @BookType
    private Dao<Author> authorDao;
    @SpyBean
    @BookType
    private Dao<Genre> genreDao;
    @SpyBean
    private JdbcOperations jdbc;
    @SpyBean
    private NamedParameterJdbcOperations jdbcParameter;

    @Test
    public void getAllBooksShouldRetrieveBookValues() {
        library.getAllBooks();

        verify(bookDao).getAll();
        verify(jdbc).query(anyString(), ArgumentMatchers.<ResultSetExtractor<Book>>any());
    }

    @Test
    public void getAllAuthorsShouldRetrieveAuthorValues() {
        library.getAllAuthors();

        verify(authorDao).getAll();
        verify(jdbc).query(anyString(), ArgumentMatchers.<ResultSetExtractor<Author>>any());
    }

    @Test
    public void getAllGenresShouldRetrieveGenreValues() {
        library.getAllGenres();

        verify(genreDao).getAll();
        verify(jdbc).query(anyString(), ArgumentMatchers.<ResultSetExtractor<Genre>>any());
    }

    @Test
    public void getBookAuthorsShouldRetrieveBookByIdAndTheirAuthors() {
        library.getBookAuthors(1L);

        verify(bookDao).getById(eq(1L));
        verify(jdbcParameter).query(anyString(), isA(SqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<Book>>any());
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirGenres() {
        library.getBookGenres(1L);

        verify(bookDao).getById(eq(1L));
        verify(jdbcParameter).query(anyString(), isA(SqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<Genre>>any());
    }

    @Test
    public void createBookShouldAddNewBook() {
        int before = JdbcTestUtils.countRowsInTable((JdbcTemplate) jdbc, "book");
        library.createBook("book title", emptySet(), emptySet());
        int after = JdbcTestUtils.countRowsInTable((JdbcTemplate) jdbc, "book");

        assertThat(before).isLessThan(after);
    }

    @Test
    public void createBookShouldThrowExceptionIfAuthorOrGenreUnavailable() {
        assertThatThrownBy(() -> library.createBook("book title", singletonList(99L), singletonList(99L)))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Referential integrity constraint violation");
    }

    @Test
    public void deleteBookShouldRemoveBook() {
        int first = JdbcTestUtils.countRowsInTable((JdbcTemplate) jdbc, "book");

        library.createBook("book title", emptySet(), emptySet());
        int middle = JdbcTestUtils.countRowsInTable((JdbcTemplate) jdbc, "book");

        library.deleteBook(2);
        int last = JdbcTestUtils.countRowsInTable((JdbcTemplate) jdbc, "book");

        assertThat(first).isLessThan(middle).isEqualTo(last);
    }
}
