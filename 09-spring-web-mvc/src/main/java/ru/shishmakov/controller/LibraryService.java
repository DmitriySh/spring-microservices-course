package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Genre;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.GenreRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LibraryService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book:" + bookId + " not found"));
    }

    @Transactional
    public void updateBook(Book data) {
        Book book = bookRepository.findById(data.getId())
                .orElseThrow(() -> new EntityNotFoundException("book:" + data.getId() + " not found"));
        book.setTitle(data.getTitle());
        book.setIsbn(data.getIsbn());
        book.setAuthors(data.getAuthors());
        book.setGenres(data.getGenres());
        bookRepository.save(book);
    }

    @Transactional
    public void createBook(Book data) {
        bookRepository.save(data);
    }

    @Transactional
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
