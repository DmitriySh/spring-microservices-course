package ru.shishmakov.service;

import org.bson.types.ObjectId;
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
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.GenreRepository;

import java.util.Optional;

import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
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
        verify(bookRepository).findAllWithFetchComments();
    }

    @Test
    public void getBookAuthorsShouldRetrieveBookByIdAndTheirAuthors() {
        libraryShell.getBookAuthors(new ObjectId());

        verify(libraryService).getBookAuthors(any(ObjectId.class));
        verify(bookRepository).findByIdWithFetchAuthors(any(ObjectId.class));
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirGenres() {
        libraryShell.getBookGenres(new ObjectId());

        verify(libraryService).getBookGenres(any(ObjectId.class));
        verify(bookRepository).findByIdWithFetchGenres(any(ObjectId.class));
    }

    @Test
    public void getBookGenresShouldRetrieveBookByIdAndTheirComments() {
        libraryShell.getBookComments(new ObjectId());

        verify(libraryService).getBookComments(any(ObjectId.class));
        verify(bookRepository).findByIdWithFetchComments(any(ObjectId.class));
    }

    @Test
    public void createBookShouldAddNewBook() {
        libraryShell.createBook("book title", "book isbn", emptySet(), emptySet());

        verify(libraryService).createBook(anyString(), anyString(), anySet(), anySet());
        verify(authorRepository).findAllById(anySet());
        verify(genreRepository).findAllById(anySet());
        verify(bookRepository).save(any(Book.class));
        verify(authorRepository).saveAll(anyIterable());
        verify(genreRepository).saveAll(anyIterable());
    }

    @Test
    public void createBookCommentShouldAddNewBookComment() {
        doReturn(Optional.of(mock(Book.class))).when(bookRepository).findById(any(ObjectId.class));

        libraryShell.createBookComment(new ObjectId(), "book comment");

        verify(libraryService).createBookComment(any(ObjectId.class), anyString());
        verify(bookRepository).findById(any(ObjectId.class));
        verify(bookRepository).save(any(Book.class));
    }


    @Test
    public void deleteBookShouldRemoveBook() {
        doReturn(Optional.of(mock(Book.class))).when(bookRepository).findByIdWithFetchGenresAuthors(any(ObjectId.class));

        libraryShell.deleteBook(new ObjectId());

        verify(libraryService).deleteBook(any(ObjectId.class));
        verify(bookRepository).findByIdWithFetchGenresAuthors(any(ObjectId.class));
        verify(bookRepository).delete(any(Book.class));
        verify(authorRepository).saveAll(anyIterable());
        verify(genreRepository).saveAll(anyIterable());
    }

    @Test
    public void deleteBookCommentShouldDeleteBookComment() {
        doReturn(Optional.of(mock(Book.class))).when(bookRepository).findById(any(ObjectId.class));

        libraryShell.deleteBookComment(new ObjectId(), new ObjectId());

        verify(libraryService).deleteComment(any(ObjectId.class), any(ObjectId.class));
        verify(bookRepository).findById(any(ObjectId.class));
        verify(bookRepository).save(any(Book.class));
    }
}
