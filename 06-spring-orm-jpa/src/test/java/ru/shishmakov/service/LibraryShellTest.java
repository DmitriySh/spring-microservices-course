package ru.shishmakov.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.dao.AuthorRepository;
import ru.shishmakov.dao.BookRepository;
import ru.shishmakov.dao.CommentRepository;
import ru.shishmakov.dao.GenreRepository;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LibraryShellTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @Autowired
    private LibraryShell libraryShell;

    @MockBean
    private Shell shell;
    @SpyBean
    private BookRepository bookRepository;
    @SpyBean
    private LibraryService libraryService;
    @SpyBean
    private AuthorRepository authorRepository;
    @SpyBean
    private GenreRepository genreRepository;
    @SpyBean
    private CommentRepository commentRepository;

    @Test
    public void getAllBooksShouldRetrieveBookValues() {
        libraryShell.getAllBooks();

        verify(libraryService).getAllBooks();
        verify(bookRepository).getAll();
    }

    @Test
    public void getAllAuthorsShouldRetrieveAuthorValues() {
        libraryShell.getAllAuthors();

        verify(libraryService).getAllAuthors();
        verify(authorRepository).getAll();
    }

    @Test
    public void getAllGenresShouldRetrieveGenreValues() {
        libraryShell.getAllGenres();

        verify(libraryService).getAllGenres();
        verify(genreRepository).getAll();
    }

    @Test
    public void getAllCommentsShouldRetrieveCommentValues() {
        libraryShell.getAllComments();

        verify(libraryService).getAllComments();
        verify(commentRepository).getAll();
    }

    @Test
    public void getBookAuthorsShouldRetrieveBookByIdAndTheirAuthors() {
        libraryShell.getBookAuthors(1L);

        verify(libraryService).getBookAuthors(eq(1L));
        verify(bookRepository).getById(eq(1L), anyMap());
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirGenres() {
        libraryShell.getBookGenres(1L);

        verify(libraryService).getBookGenres(eq(1L));
        verify(bookRepository).getById(eq(1L), anyMap());
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirComments() {
        libraryShell.getBookComments(1L);

        verify(libraryService).getBookComments(eq(1L));
        verify(bookRepository).getById(eq(1L), anyMap());
    }

    @Test
    @Transactional
    public void createBookCommentShouldAddNewBookComment() {
        long before = commentRepository.count();
        libraryShell.createBookComment(1L, "book comment N");
        long after = commentRepository.count();

        assertThat(before).isLessThan(after);
        verify(libraryService).createBookComment(eq(1L), anyString());
    }

    @Test
    @Transactional
    public void deleteBookCommentShouldDeleteBookComment() {
        long first = commentRepository.count();

        libraryShell.createBookComment(1L, "comment 1");
        long commentId = commentRepository.maxCommentId();

        long middle = commentRepository.count();
        libraryShell.deleteBookComment(commentId);
        long last = commentRepository.count();

        assertThat(first).isLessThan(middle).isEqualTo(last);
        verify(libraryService).createBookComment(eq(1L), anyString());
        verify(libraryService).deleteComment(eq(commentId));
    }

    @Test
    @Transactional
    public void createBookShouldAddNewBook() {
        long before = bookRepository.count();
        libraryShell.createBook("book title", "book isbn 1", emptySet(), emptySet());
        long after = bookRepository.count();

        assertThat(before).isLessThan(after);
        verify(libraryService).createBook(anyString(), anyString(), anySet(), anySet());
    }

    @Test
    @Transactional
    public void deleteBookShouldRemoveBook() {
        long first = bookRepository.count();

        libraryShell.createBook("book title", "book isbn 3", emptySet(), emptySet());
        long bookId = bookRepository.maxBookId();

        long middle = bookRepository.count();

        libraryShell.deleteBook(bookId);
        long last = bookRepository.count();

        assertThat(first).isLessThan(middle).isEqualTo(last);
        verify(libraryService).createBook(anyString(), anyString(), anySet(), anySet());
        verify(libraryService).deleteBook(eq(bookId));
    }
}
