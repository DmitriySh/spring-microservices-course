package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;
import ru.shishmakov.dto.BookDto;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.GenreRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LibraryService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAllWithFetchGenresAuthors();
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book:" + bookId + " not found"));
    }

    @Transactional
    public Book updateBook(BookDto data) {
        Book book = bookRepository.findByIdWithFetchGenresAuthors(data.getId())
                .orElseThrow(() -> new EntityNotFoundException("book:" + data.getId() + " not found"));
        Set<Author> oldAuthors = book.getAuthors();
        Set<Author> newAuthors = findAuthors(data.getAuthors());
        oldAuthors.removeAll(newAuthors);
        oldAuthors.forEach(a -> a.getBooks().remove(book));
        newAuthors.forEach(a -> a.getBooks().add(book));

        Set<Genre> oldGenres = book.getGenres();
        Set<Genre> newGenres = findGenres(data.getGenres());
        oldGenres.removeAll(newGenres);
        oldGenres.forEach(g -> g.getBooks().remove(book));
        newGenres.forEach(g -> g.getBooks().add(book));

        book.setTitle(data.getTitle());
        book.setIsbn(data.getIsbn());
        book.setAuthors(newAuthors);
        book.setGenres(newGenres);
        bookRepository.save(book);
        authorRepository.saveAll(oldAuthors);
        genreRepository.saveAll(oldGenres);
        log.info("update book: {}", book);
        return book;
    }

    @Transactional
    public Book createBook(BookDto data) {
        Book book = Book.builder()
                .title(data.getTitle())
                .isbn(data.getIsbn())
                .authors(findAuthors(data.getAuthors()))
                .genres(findGenres(data.getGenres()))
                .build();
        book.getAuthors().forEach(a -> a.getBooks().add(book));
        book.getGenres().forEach(g -> g.getBooks().add(book));
        log.info("save book: {}", book);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBookById(Long bookId) {
        bookRepository.findByIdWithFetchGenresAuthors(bookId).ifPresentOrElse(book -> {
            book.removeAllAuthors();
            book.removeAllGenres();
            bookRepository.delete(book);
            log.info("delete book: {}", book);
        }, () -> new EntityNotFoundException("book:" + bookId + " not found"));
    }

    private Set<Genre> findGenres(Set<Long> genreIds) {
        return ofNullable(genreIds)
                .map(genreRepository::findAllByIdWithFetchBooks)
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }

    private Set<Author> findAuthors(Set<Long> authorIds) {
        return ofNullable(authorIds)
                .map(authorRepository::findAllByIdWithFetchBooks)
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }
}
