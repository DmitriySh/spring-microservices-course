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
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.CommentRepository;
import ru.shishmakov.repository.GenreRepository;

import java.util.Optional;

import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryShellTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @Autowired
    private LibraryShell libraryShell;
    @SpyBean
    private LibraryService libraryService;

    @MockBean
    private Shell shell;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Test
    public void getAllBooksShouldRetrieveBookValues() {
        libraryShell.getAllBooks();

        verify(libraryService).getAllBooks();
        verify(bookRepository).findAll();
    }

    @Test
    public void getAllAuthorsShouldRetrieveAuthorValues() {
        libraryShell.getAllAuthors();

        verify(libraryService).getAllAuthors();
        verify(authorRepository).findAll();
    }

    @Test
    public void getAllGenresShouldRetrieveGenreValues() {
        libraryShell.getAllGenres();

        verify(libraryService).getAllGenres();
        verify(genreRepository).findAll();
    }

    @Test
    public void getAllCommentsShouldRetrieveCommentValues() {
        libraryShell.getAllComments();

        verify(libraryService).getAllComments();
        verify(commentRepository).findAll();
    }

    @Test
    public void getBookAuthorsShouldRetrieveBookByIdAndTheirAuthors() {
        libraryShell.getBookAuthors(1L);

        verify(libraryService).getBookAuthors(eq(1L));
        verify(bookRepository).findByIdWithFetchAuthors(eq(1L));
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirGenres() {
        libraryShell.getBookGenres(1L);

        verify(libraryService).getBookGenres(eq(1L));
        verify(bookRepository).findByIdWithFetchGenres(eq(1L));
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirComments() {
        libraryShell.getBookComments(1L);

        verify(libraryService).getBookComments(eq(1L));
        verify(bookRepository).findByIdWithFetchComments(eq(1L));
    }

    @Test
    public void createBookCommentShouldAddNewBookComment() {
        libraryShell.createBookComment(1L, "book comment N");

        verify(libraryService).createBookComment(eq(1L), anyString());
        verify(bookRepository).findById(eq(1L));
    }

    @Test
    public void deleteBookCommentShouldDeleteBookComment() {
        Comment comment = Comment.builder().book(mock(Book.class)).build();
        doReturn(Optional.of(comment)).when(commentRepository).findByIdWithFetchBook(anyLong());

        libraryShell.deleteBookComment(1L);

        verify(libraryService).deleteComment(eq(1L));
        verify(commentRepository).findByIdWithFetchBook(eq(1L));
        verify(commentRepository).delete(any(Comment.class));
    }

    @Test
    public void createBookShouldAddNewBook() {
        libraryShell.createBook("book title", "book isbn 1", emptySet(), emptySet());

        verify(libraryService).createBook(anyString(), anyString(), anySet(), anySet());
        verify(authorRepository).findAllById(anySet());
        verify(genreRepository).findAllById(anySet());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void deleteBookShouldRemoveBook() {
        doReturn(Optional.of(mock(Book.class))).when(bookRepository).findByIdWithFetchCommentsGenresAuthors(anyLong());

        libraryShell.deleteBook(1L);

        verify(libraryService).deleteBook(eq(1L));
        verify(bookRepository).findByIdWithFetchCommentsGenresAuthors(eq(1L));
        verify(bookRepository).delete(any(Book.class));
    }
}
